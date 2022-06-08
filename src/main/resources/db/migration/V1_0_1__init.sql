CREATE TABLE sys.words
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL UNIQUE,
    first_letter VARCHAR(1) NOT NULL
);


CREATE TABLE sys.indexes
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    first_letter         VARCHAR(1) NOT NULL,
    other_letter         VARCHAR(255) NOT NULL
);


CREATE TABLE sys.idwords_idindex
(
    id_index              INT NOT NULL,
    id_word        INT NOT NULL
);
ALTER TABLE sys.idwords_idindex
    ADD CONSTRAINT fk_words_id FOREIGN KEY (id_word) REFERENCES words (id);
ALTER TABLE sys.idwords_idindex
    ADD CONSTRAINT fk_indexes_id FOREIGN KEY (id_index) REFERENCES indexes (id);