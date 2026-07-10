package com.example.voting.adapters.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.voting.adapters.persistence.jpa.entity.VoterEntity;

public interface VoterJpaRepository extends JpaRepository<VoterEntity, UUID> {

    boolean existsByName(String name);
}
