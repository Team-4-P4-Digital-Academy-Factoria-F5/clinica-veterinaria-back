-- Insert Roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');


INSERT INTO users (email, password) VALUES ('maria.garcia@example.com', '$2a$10$YXzHtME0AnzqL9U9rA6iFONnCyIlQEkpmN2/gKfUBFlSzuZQ2GcvC'); -- "securePassword123"
INSERT INTO roles_users (user_id, role_id) VALUES ((SELECT id_user FROM users WHERE email = 'maria.garcia@example.com'), (SELECT id_role FROM roles WHERE name = 'ROLE_USER'));
INSERT INTO profiles (dni, name, first_surname, second_surname, phone_number, user_id) VALUES ('12345675X', 'Maria', 'Garcia', 'Lopez', '612345678', (SELECT id_user FROM users WHERE email = 'maria.garcia@example.com'));

INSERT INTO users (email, password) VALUES ('maria.garcia2@example.com', '$2a$10$YXzHtME0AnzqL9U9rA6iFONnCyIlQEkpmN2/gKfUBFlSzuZQ2GcvC'); -- "securePassword123"
INSERT INTO roles_users (user_id, role_id) VALUES ((SELECT id_user FROM users WHERE email = 'maria.garcia2@example.com'), (SELECT id_role FROM roles WHERE name = 'ROLE_ADMIN'));
INSERT INTO profiles (dni, name, first_surname, second_surname, phone_number, user_id) VALUES ('87654321Z', 'Admin', 'System', 'Root', '699999999', (SELECT id_user FROM users WHERE email = 'maria.garcia2@example.com'));