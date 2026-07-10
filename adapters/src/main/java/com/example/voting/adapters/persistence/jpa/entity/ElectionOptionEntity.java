package com.example.voting.adapters.persistence.jpa.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "election_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectionOptionEntity {

    @Id
    private UUID id;

    @Column(name = "election_id", nullable = false)
    private UUID electionId;

    @Column(name = "label", nullable = false, length = 200)
    private String label;
}
