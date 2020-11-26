-- Database: covid_alert_db
ALTER SYSTEM SET wal_level = logical;

DROP DATABASE IF EXISTS covid_alert_db;

CREATE DATABASE covid_alert_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
\c covid_alert_db

CREATE TABLE users (
	id_keycloak varchar(50) not null,
	contact_mail varchar(50) not null,
	constraint cp_keycloak PRIMARY KEY (id_keycloak)
);

CREATE TABLE locations (
	id_location integer not null,
	latitude double precision not null,
	longitude double precision not null,
	location_date timestamp without time zone not null,
	constraint cp_location PRIMARY KEY (id_location)
);

CREATE TABLE user_locations
(
    id_keycloak varchar(50) NOT NULL REFERENCES users (id_keycloak),
    id_location integer NOT NULL REFERENCES locations (id_location)
);

CREATE TABLE case_type (
	id_case_type integer not null,
	description varchar(20) not null,
	constraint cp_case_type PRIMARY KEY (id_case_type)
);

CREATE TABLE covid_info (
	id_keycloak varchar(50) not null,
	id_case_type integer not null,
	reporting_date timestamp without time zone not null,
	constraint cp_covid_info PRIMARY KEY (id_keycloak, id_case_type, reporting_date),
	constraint ce1_covid_info FOREIGN KEY (id_keycloak) REFERENCES users(id_keycloak),
	constraint ce2_covid_info FOREIGN KEY (id_case_type) REFERENCES case_type(id_case_type)
);