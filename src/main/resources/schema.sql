CREATE TABLE IF NOT EXISTS locations (
   id_location integer not null,
   latitude double precision not null,
   longitude double precision not null,
   location_date timestamp without time zone not null,
   constraint cp_location PRIMARY KEY (id_location)
);