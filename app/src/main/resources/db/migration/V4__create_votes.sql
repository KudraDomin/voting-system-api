CREATE TABLE votes (
    id          UUID NOT NULL,
    voter_id    UUID NOT NULL,
    election_id UUID NOT NULL,
    option_id   UUID NOT NULL,
    CONSTRAINT pk_votes PRIMARY KEY (id),
    CONSTRAINT fk_votes_voter FOREIGN KEY (voter_id) REFERENCES voters (id),
    CONSTRAINT fk_votes_election FOREIGN KEY (election_id) REFERENCES elections (id),
    CONSTRAINT fk_votes_option FOREIGN KEY (option_id) REFERENCES election_options (id),
    CONSTRAINT uq_votes_voter_election UNIQUE (voter_id, election_id)
);
