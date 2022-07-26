USE kanclOnline;

CREATE TABLE TestTable (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    testString VARCHAR(50) NOT NULL,
    INDEX(id)
);

INSERT INTO TestTable(id, testString) VALUES (1, 'Hello world!');

