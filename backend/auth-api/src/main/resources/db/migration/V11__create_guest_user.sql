INSERT INTO users (id, email, user_name, password, role)
SELECT gen_random_uuid(), 'usertest@test.com', 'User_Test', '$2a$10$dC8goURROnpcReXXuRphkOo3ZD7fmVmNlm8CMFoPqDhvDKDe1fpHm', 'ROLE_GUEST'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'usertest@test.com'
);
