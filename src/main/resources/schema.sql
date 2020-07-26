DROP TABLE IF EXISTS Location CASCADE;
DROP TABLE IF EXISTS Equipment CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;

CREATE TABLE Location (
    id BIGSERIAL NOT NULL,
    address VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Equipment (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    type VARCHAR(255) NOT NULL,
    located_at_id INT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE Employee (
    id BIGSERIAL NOT NULL,
    job VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    operates_id INT NOT NULL,
    works_at_id INT NOT NULL,
    PRIMARY KEY(id)
);

ALTER TABLE Equipment
    ADD CONSTRAINT fk_located_at
        FOREIGN KEY(located_at_id)
            REFERENCES Location;

ALTER TABLE Employee
   ADD CONSTRAINT fk_operates
       FOREIGN KEY(operates_id)
           REFERENCES Equipment;

ALTER TABLE Employee
   ADD CONSTRAINT fk_works_at
       FOREIGN KEY(works_at_id)
           REFERENCES Location;