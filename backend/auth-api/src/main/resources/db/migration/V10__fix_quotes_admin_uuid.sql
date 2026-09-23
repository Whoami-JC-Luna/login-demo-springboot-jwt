UPDATE quotes
SET user_id = (SELECT id FROM users WHERE user_name = 'user_JonA')
WHERE user_id IS NULL;
