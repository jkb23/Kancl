CREATE TABLE Comment
(
    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    author  VARCHAR(50)                    NOT NULL,
    message VARCHAR(300)                   NOT NULL
);
