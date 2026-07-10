package com.example.voting.adapters.persistence.jpa;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.voting.adapters.persistence.jpa.mapper.VoterEntityMapper;
import com.example.voting.adapters.persistence.jpa.repository.VoterJpaRepository;
import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.spi.VoterSpiPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VoterPersistenceAdapter implements VoterSpiPort {

    private final VoterJpaRepository voterJpaRepository;

    private final VoterEntityMapper voterEntityMapper;

    @Override
    public Voter save(Voter voter) {
        var persistedEntity = voterJpaRepository.save(voterEntityMapper.toEntity(voter));

        return voterEntityMapper.toDomain(persistedEntity);
    }

    @Override
    public Optional<Voter> findById(VoterId voterId) {
        return voterJpaRepository.findById(voterId.value())
                .map(voterEntityMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return voterJpaRepository.existsByName(name);
    }
}
