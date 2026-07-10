CREATE TABLE elections (
    id    UUID         NOT NULL,
    title VARCHAR(200) NOT NULL,
    CONSTRAINT pk_elections PRIMARY KEY (id)
);
