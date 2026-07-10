package com.example.voting.adapters.persistence.jpa.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "votes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_votes_voter_election",
                columnNames = {"voter_id", "election_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

    @Id
    private UUID id;

    @Column(name = "voter_id", nullable = false)
    private UUID voterId;

    @Column(name = "election_id", nullable = false)
    private UUID electionId;

    @Column(name = "option_id", nullable = false)
    private UUID optionId;
}
