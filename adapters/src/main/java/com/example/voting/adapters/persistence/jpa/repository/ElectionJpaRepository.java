package com.example.voting.adapters.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.voting.adapters.persistence.jpa.entity.ElectionEntity;

public interface ElectionJpaRepository extends JpaRepository<ElectionEntity, UUID> {
}
