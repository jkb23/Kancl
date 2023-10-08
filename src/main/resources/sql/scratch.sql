CREATE TABLE DatabaseSchemaHash
(
    hash VARCHAR(300)
);

CREATE TABLE AppUser
(
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username            VARCHAR(100) UNIQUE,
    password            VARCHAR(100) NOT NULL,
    email               VARCHAR(100),
    avatar              VARCHAR(100),
    bg_color            VARCHAR(50),
    bad_login_count     INTEGER DEFAULT 0,
    bad_login_timestamp TIMESTAMP,
    user_status         VARCHAR(200)
);

INSERT INTO AppUser (username, password, email, bg_color, user_status)
      VALUES('correct', '15a596e3c98c407e043751ff3b21ff0358a1bdfdf3fe948b1523893a8e5de2e8', 'neexistuju@baf.com', 'blue', 'Mam se dobre!')
