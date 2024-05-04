create schema test;
create table test.weatherStatus
(
    id long primary key ,
    city varchar(50) not null,
    country varchar(50) not null,
    weatherDesc varchar(50) not null
);