CREATE TABLE DatabaseSchemaHash
(
    hash VARCHAR(300)
);

CREATE TABLE Comment
(
    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    author  VARCHAR(50)  NOT NULL,
    message VARCHAR(300) NOT NULL
);

CREATE TABLE User
(
    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) AS IDENTITY UNIQUE KEY,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(100),
    avatar VARCHAR(100),
    avatar_color CHAR(6),
    bad_login_count INTEGER DEFAULT 0
    time_timestamp timestamp
)


