INSERT INTO location (name, address) VALUES ('Debrecen', 'Péterfia utca 20')
INSERT INTO location (name, address) VALUES ('Ondód', 'Szekeres utca 15')
INSERT INTO location (name, address) VALUES ('Miskolc', 'Deák tér 10')
INSERT INTO location (name, address) VALUES ('Budapest', 'Pünkösdfürdő utca 50')

INSERT INTO equipment (type, located_at_id) VALUES ('OVEN', 1)
INSERT INTO equipment (type, located_at_id) VALUES ('OVEN', 3)
INSERT INTO equipment (type, located_at_id) VALUES ('CASH_REGISTER', 2)
INSERT INTO equipment (type, located_at_id) VALUES ('CASH_REGISTER', 3)
INSERT INTO equipment (type, located_at_id) VALUES ('CASH_REGISTER', 4)

INSERT INTO employee (name, job, operates_id, works_at_id) VALUES ('Joe', 'COOK', 1, 1)