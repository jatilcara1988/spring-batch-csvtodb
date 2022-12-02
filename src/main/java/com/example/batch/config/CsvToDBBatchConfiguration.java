package com.example.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.batch.model.Province;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CsvToDBBatchConfiguration {
	
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final DataSource dataSource;

	@Value("${batch.file.location.input}")
	private String fileLocation;

	private static final String INSERT_SCRIPT = "insert into Province (id, fullName, source, isoId, name, category, isoName, centroideLat, centroideLon) values (:id, :fullName, :source, :isoId, :name, :category, :isoName, :centroideLat, :centroideLon)";

	@Bean
	public FlatFileItemReader<Province> readFromCsv() {
		return new FlatFileItemReaderBuilder<Province>()
				.name("provinceItemReader")
				.resource(new FileSystemResource(fileLocation))
				.lineMapper(new DefaultLineMapper<Province>() {
					{
						setLineTokenizer(new DelimitedLineTokenizer() {
							{
								setNames(Province.fields());
							}
						});
						setFieldSetMapper(new BeanWrapperFieldSetMapper<Province>() {
							{
								setTargetType(Province.class);
							}
						});
					}
				})
				.build();
	}

	@Bean
	public JdbcBatchItemWriter<Province> writerIntoDB() {
		return new JdbcBatchItemWriterBuilder<Province>()
				.dataSource(dataSource)
				.sql(INSERT_SCRIPT)
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Province>())
				.build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
				.<Province, Province>chunk(10)
				.reader(readFromCsv())
				.writer(writerIntoDB())
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.flow(step())
				.end()
				.build();
	}

}
