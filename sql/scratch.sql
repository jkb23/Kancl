USE kanclOnline;

CREATE TABLE Comment (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    author VARCHAR(50) NOT NULL,
    message VARCHAR(300) NOT NULL,
    INDEX(id)
);
