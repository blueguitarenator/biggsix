create table "password"
("id" bigserial NOT NULL PRIMARY KEY,
"hashed_password" VARCHAR(254),
"salt" VARCHAR(254)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "password"
  OWNER TO biggsix;

create table "person"
("id" bigserial NOT NULL PRIMARY KEY,
"email" VARCHAR(254) NOT NULL,
"first_name" VARCHAR(254) NOT NULL,
"last_name" VARCHAR(254) NOT NULL,
"password_id" bigint NOT NULL,
"row_timestamp" character varying NOT NULL,
    CONSTRAINT "password_FK" FOREIGN KEY (password_id)
          REFERENCES "password" (id) MATCH SIMPLE
          ON UPDATE RESTRICT ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "person"
  OWNER TO biggsix;



CREATE TABLE "location"
(
  id bigserial NOT NULL,
  "name" character varying NOT NULL,
  "address" character varying NOT NULL,
  "phone" character varying NOT NULL,
  "row_timestamp" character varying NOT NULL,
  CONSTRAINT "location_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "location"
  OWNER TO biggsix;

CREATE TABLE "provider"
(
    id bigserial NOT NULL,
    "first_name" character varying NOT NULL,
    "last_name" character varying NOT NULL,
    "phone" character varying NOT NULL,
    "location_id" bigint NOT NULL,
    "row_timestamp" character varying NOT NULL,
    CONSTRAINT "provider_pkey" PRIMARY KEY (id),
    CONSTRAINT "location_FK" FOREIGN KEY (location_id)
          REFERENCES "location" (id) MATCH SIMPLE
          ON UPDATE RESTRICT ON DELETE CASCADE
)
WITH (
    OIDS=FALSE
);
ALTER TABLE "provider"
    OWNER TO biggsix;


CREATE TABLE "customer"
(
    id bigserial NOT NULL,
    "first_name" character varying NOT NULL,
    "last_name" character varying NOT NULL,
    "phone" character varying NOT NULL,
    "location_id" bigint NOT NULL,
    "row_timestamp" character varying NOT NULL,
    CONSTRAINT "customer_pkey" PRIMARY KEY (id),
    CONSTRAINT "location_FK" FOREIGN KEY (location_id)
          REFERENCES "location" (id) MATCH SIMPLE
          ON UPDATE RESTRICT ON DELETE CASCADE
)
WITH (
    OIDS=FALSE
);
ALTER TABLE "customer"
    OWNER TO biggsix;

CREATE TABLE "time_slot"
(
    id bigserial NOT NULL,
    "start" integer NOT NULL,
    "end" integer NOT NULL,
    "is_available" boolean NOT NULL,
    CONSTRAINT "time_slot_pkey" PRIMARY KEY (id)
)
WITH (
    OIDS=FALSE
);
ALTER TABLE "time_slot"
    OWNER TO biggsix;



CREATE TABLE "appointment"
(
    id bigserial NOT NULL,
    "date" date NOT NULL,
    "provider_id" bigint NOT NULL,
    "time_slot_id" bigint NOT NULL,
    "row_timestamp" character varying NOT NULL,
    CONSTRAINT "appointment_pkey" PRIMARY KEY (id),
    CONSTRAINT "provider_FK" FOREIGN KEY (provider_id)
          REFERENCES "provider" (id) MATCH SIMPLE,
    CONSTRAINT "time_slot_FK" FOREIGN KEY (time_slot_id)
          REFERENCES "time_slot" (id) MATCH SIMPLE
          ON UPDATE RESTRICT ON DELETE CASCADE
)
WITH (
    OIDS=FALSE
);
ALTER TABLE "appointment"
    OWNER TO biggsix;

CREATE TABLE "appt_cust"
(
    id bigserial NOT NULL,
    customer_id bigint NOT NULL,
    appointment_id bigint NOT NULL,
    CONSTRAINT "appt_cust_pkey" PRIMARY KEY (id),
    CONSTRAINT "customer_FK" FOREIGN KEY (customer_id)
          REFERENCES "customer" (id) MATCH SIMPLE,
    CONSTRAINT "appointment_FK" FOREIGN KEY (appointment_id)
          REFERENCES "appointment" (id) MATCH SIMPLE
)
WITH (
    OIDS=FALSE
);
ALTER TABLE "appt_cust"
    OWNER TO biggsix;


