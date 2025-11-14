INSERT INTO users (id, username, password_hash, full_name, role)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'user', 'userpass', 'Demo User', 'USER'),
  ('00000000-0000-0000-0000-000000000002', 'officer', 'officerpass', 'Compliance Officer', 'OFFICER')
ON CONFLICT (username) DO NOTHING;
