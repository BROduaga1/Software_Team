INSERT INTO training_types (id, name) OVERRIDING SYSTEM VALUE
VALUES
    (1, 'Yoga'),
    (2, 'Pilates'),
    (3, 'Cardio'),
    (4, 'Strength Training'),
    (5, 'HIIT')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (is_active, id, first_name, last_name, password, username) OVERRIDING SYSTEM VALUE VALUES
('t', 1, 'admin', 'admin', '$2a$10$e2iL9YNlAmjj85eRAngScO8Cgu644cjVEIHKFZFpycobmcGcN5OW.', 'admin.admin')
ON CONFLICT (id) DO NOTHING;

INSERT INTO admins (id, user_id) OVERRIDING SYSTEM VALUE VALUES
(1, 1)
ON CONFLICT (id) DO NOTHING;
