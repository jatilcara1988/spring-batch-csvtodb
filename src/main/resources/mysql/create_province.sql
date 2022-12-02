## DROP TABLE batchexampledb.Province;

CREATE TABLE batchexampledb.Province (
    id int not null,
    fullName varchar(100) not null,
    source varchar(5) not null,
    isoId varchar(5) not null,
    name varchar(100) not null,
    category varchar(20) not null,
    isoName varchar(50) not null,
    centroideLat varchar(20) not null,
    centroideLon varchar(20) not null
);