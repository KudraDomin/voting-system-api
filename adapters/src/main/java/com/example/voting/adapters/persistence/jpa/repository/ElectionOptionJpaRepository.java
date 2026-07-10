package com.example.voting.adapters.persistence.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.voting.adapters.persistence.jpa.entity.ElectionOptionEntity;

public interface ElectionOptionJpaRepository extends JpaRepository<ElectionOptionEntity, UUID> {

    List<ElectionOptionEntity> findByElectionId(UUID electionId);
}
