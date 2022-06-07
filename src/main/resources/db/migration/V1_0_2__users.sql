CREATE TABLE sys.users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    login    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name    VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL,
    role    INT NOT NULL
);