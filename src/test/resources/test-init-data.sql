DELETE FROM training_types;
INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'Yoga');
INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'Pilates');
INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE VALUES (3, 'Cardio');
INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE VALUES (4, 'Strength Training');
INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE VALUES (5, 'HIIT');

DELETE FROM users;
DELETE FROM trainees;
INSERT INTO   users (id, is_active,  first_name, last_name, password, username) OVERRIDING SYSTEM VALUE VALUES (97, true, 'admin', 'admin', '$2a$10$e2iL9YNlAmjj85eRAngScO8Cgu644cjVEIHKFZFpycobmcGcN5OW.', 'admin.admin');
INSERT INTO admins (id, user_id) OVERRIDING SYSTEM VALUE VALUES (1, 97);
