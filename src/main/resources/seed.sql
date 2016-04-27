-- User: test1@test.com, Password: password1
INSERT INTO password(
id, hashed_password, salt)
VALUES (1,'$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC', '$2a$10$U3gBQ50FY5qiQ5XeQKgWwO');

INSERT INTO person(
id, email, first_name, last_name, password_id, row_timestamp)
VALUES (1, 'test1@test.com', 'john', 'doe', 1, 'now');