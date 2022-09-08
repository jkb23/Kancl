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

CREATE TABLE AppUser
(
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username            VARCHAR(100) UNIQUE,
    password            VARCHAR(100) NOT NULL,
    email               VARCHAR(100),
    nickname            VARCHAR(100),
    avatar              VARCHAR(100),
    avatar_color        CHAR(6),
    bad_login_count     INTEGER DEFAULT 0,
    bad_login_timestamp TIMESTAMP
);

INSERT INTO AppUser (username, password, email)
      VALUES('correct', '15a596e3c98c407e043751ff3b21ff0358a1bdfdf3fe948b1523893a8e5de2e8', 'neexistuju@baf.com')
