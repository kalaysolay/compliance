-- sql
INSERT INTO users (id, username, password_hash, full_name, role)
SELECT '00000000-0000-0000-0000-000000000001', 'user', 'userpass', 'Demo User', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

INSERT INTO users (id, username, password_hash, full_name, role)
SELECT '00000000-0000-0000-0000-000000000002', 'officer', 'officerpass', 'Compliance Officer', 'OFFICER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'officer');