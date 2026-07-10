CREATE TABLE election_options (
    id          UUID         NOT NULL,
    election_id UUID         NOT NULL,
    label       VARCHAR(200) NOT NULL,
    CONSTRAINT pk_election_options PRIMARY KEY (id),
    CONSTRAINT fk_election_options_election FOREIGN KEY (election_id) REFERENCES elections (id)
);

CREATE INDEX idx_election_options_election ON election_options (election_id);
