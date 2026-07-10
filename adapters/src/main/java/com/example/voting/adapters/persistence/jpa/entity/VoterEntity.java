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
@Table(name = "voters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoterEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "status", nullable = false, length = 20)
    private String status;
}
