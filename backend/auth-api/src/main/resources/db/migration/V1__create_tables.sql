CREATE TABLE users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE quotes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    texto VARCHAR(500) NOT NULL,
    autor VARCHAR(100) NOT NULL
);

CREATE TABLE firmas (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    message VARCHAR(500) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT now(),
    user_id UUID NOT NULL REFERENCES users(id)
);
