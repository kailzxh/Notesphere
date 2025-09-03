-- Sample seed data (optional)
-- Create a test user
INSERT INTO users (id, email, password_hash)
VALUES (gen_random_uuid(), 'test@example.com', crypt('Test1234!', gen_salt('bf')));

-- Create sample notes for the user (replace USER_ID with actual id)
-- INSERT INTO notes (user_id, title, content) VALUES ('USER_ID', 'Sample Note', 'This is a sample');
