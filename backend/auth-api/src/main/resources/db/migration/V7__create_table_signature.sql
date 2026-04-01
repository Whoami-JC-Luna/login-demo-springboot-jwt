ALTER TABLE signatures
    RENAME COLUMN date TO created_at;

ALTER TABLE signatures
    ADD COLUMN author     VARCHAR(100) NOT NULL DEFAULT 'Anonymous',
    ADD COLUMN updated_at TIMESTAMP    NOT NULL DEFAULT now();

ALTER TABLE signatures
    ADD CONSTRAINT signatures_user_id_unique UNIQUE (user_id);
