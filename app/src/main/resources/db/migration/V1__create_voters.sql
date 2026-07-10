CREATE TABLE voters (
    id     UUID         NOT NULL,
    name   VARCHAR(120) NOT NULL,
    status VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_voters PRIMARY KEY (id),
    CONSTRAINT uq_voters_name UNIQUE (name)
);
