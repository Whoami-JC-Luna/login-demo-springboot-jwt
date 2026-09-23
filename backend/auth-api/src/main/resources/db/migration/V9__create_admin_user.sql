INSERT INTO users (id, user_name, email, password, role)
SELECT gen_random_uuid(), 'user_JonA', 'useradmin@testadmin.com', '$2a$10$2o8uLG3YHtQJzHbVvqBoCuH9y9bP5E6acFtkJsxI2Rp5b.uyVtQP2', 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE user_name = 'user_JonA'
);
