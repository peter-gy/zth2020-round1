INSERT INTO location (id, name, address) VALUES (1, 'Debrecen', 'Péterfia utca 20')
INSERT INTO location (id, name, address) VALUES (2, 'Ondód', 'Szekeres utca 15')
INSERT INTO location (id, name, address) VALUES (3, 'Miskolc', 'Deák tér 10')
INSERT INTO location (id, name, address) VALUES (4, 'Budapest', 'Pünkösdfürdő utca 50')

INSERT INTO equipment (id, type, located_at_id) VALUES (1, 'OVEN', 1)
INSERT INTO equipment (id, type, located_at_id) VALUES (2, 'OVEN', 3)
INSERT INTO equipment (id, type, located_at_id) VALUES (3, 'CASH_REGISTER', 2)
INSERT INTO equipment (id, type, located_at_id) VALUES (4, 'CASH_REGISTER', 3)
INSERT INTO equipment (id, type, located_at_id) VALUES (5, 'CASH_REGISTER', 4)

INSERT INTO employee (id, name, job, operates_id, works_at_id) VALUES (1, 'Joe', 'COOK', 1, 1)