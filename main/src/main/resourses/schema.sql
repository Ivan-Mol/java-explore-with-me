DROP TABLE if EXISTS users,categories,events;
CREATE TABLE if NOT EXISTS users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    email varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(256),
    annotation TEXT NOT NULL,
    category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE NOT NULL,
    paid BOOLEAN NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT REFERENCES users(id) ON DELETE CASCADE
    description TEXT ,
    participant_limit INT NOT NULL,
    state VARCHAR(64) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    moderation BOOLEAN,
);