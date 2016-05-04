-- User: test1@test.com, Password: password1
INSERT INTO password(hashed_password, salt)
VALUES ('$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC', '$2a$10$U3gBQ50FY5qiQ5XeQKgWwO');

INSERT INTO person(id, email, first_name, last_name, phone, password_id, row_timestamp)
VALUES ('schwep@test.com', 'Rich', 'Schwepker', '345-563-2342', 1, 'now');

INSERT INTO time_slot(start_time, end_time, is_available) VALUES (6, 7, true);

INSERT INTO location(name, address, phone, row_timestamp) VALUES ('dardenne prairie', '234 main street', '234-234-2344', 'now');

INSERT INTO provider(location_id, person_id, row_timestamp) VALUES (1, 1, 'now');

INSERT INTO appointment(date, provider_id, time_slot_id, row_timestamp) VALUES ('now', 2, 1, 'now');