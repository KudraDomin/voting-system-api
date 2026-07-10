package com.example.voting.adapters.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.voting.adapters.persistence.jpa.entity.VoteEntity;

public interface VoteJpaRepository extends JpaRepository<VoteEntity, UUID> {

    boolean existsByVoterIdAndElectionId(UUID voterId, UUID electionId);
}
