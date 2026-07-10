package com.example.voting.adapters.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.voting.adapters.persistence.jpa.mapper.ElectionEntityMapper;
import com.example.voting.adapters.persistence.jpa.mapper.ElectionOptionEntityMapper;
import com.example.voting.adapters.persistence.jpa.repository.ElectionJpaRepository;
import com.example.voting.adapters.persistence.jpa.repository.ElectionOptionJpaRepository;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.ElectionOption;
import com.example.voting.usecases.spi.ElectionSpiPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ElectionPersistenceAdapter implements ElectionSpiPort {

    private final ElectionJpaRepository electionJpaRepository;

    private final ElectionOptionJpaRepository electionOptionJpaRepository;

    private final ElectionEntityMapper electionEntityMapper;

    private final ElectionOptionEntityMapper electionOptionEntityMapper;

    @Override
    @Transactional
    public Election save(Election election) {
        electionJpaRepository.save(electionEntityMapper.toEntity(election));
        saveOptions(election);

        return election;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Election> findById(ElectionId electionId) {
        return electionJpaRepository.findById(electionId.value())
                .map(entity -> electionEntityMapper.toDomain(entity, loadOptions(electionId)));
    }

    private void saveOptions(Election election) {
        var optionEntities = election.options().stream()
                .map(electionOptionEntityMapper::toEntity)
                .toList();

        electionOptionJpaRepository.saveAll(optionEntities);
    }

    private List<ElectionOption> loadOptions(ElectionId electionId) {
        return electionOptionJpaRepository.findByElectionId(electionId.value()).stream()
                .map(electionOptionEntityMapper::toDomain)
                .toList();
    }
}
