package com.example.batch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Province {

	private Long id;
	private String fullName;
	private String source;
	private String isoId;
	private String name;
	private String category;
	private String isoName;
	private String centroideLat;
	private String centroideLon;

	public static String[] fields() {
		return new String[] {
				"fullName", "source", "isoId", "name", "id", "category", "isoName", "centroideLat", "centroideLon"
				};
	}

	@Override
	public String toString() {
		return String.format("{ ID: %d FULLNAME: %s SOURCE: %s ISOID: %s NAME: %s CATEGORY: %s ISONAME: %s CENTROIDELAT: %d CENTROIDELON: %d }",
				this.id, this.fullName, this.source, this.isoId, this.name, this.category, this.isoName, this.centroideLat, this.centroideLon);
	}

}
