package com.example.voting.adapters.persistence.jpa;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.example.voting.adapters.persistence.jpa.mapper.VoteEntityMapper;
import com.example.voting.adapters.persistence.jpa.repository.VoteJpaRepository;
import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.Vote;
import com.example.voting.core.model.VoterId;
import com.example.voting.usecases.spi.VoteSpiPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VotePersistenceAdapter implements VoteSpiPort {

    private final VoteJpaRepository voteJpaRepository;

    private final VoteEntityMapper voteEntityMapper;

    @Override
    public Vote save(Vote vote) {
        try {
            var persistedEntity = voteJpaRepository.saveAndFlush(voteEntityMapper.toEntity(vote));

            return voteEntityMapper.toDomain(persistedEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateVoteException(vote.voterId(), vote.electionId());
        }
    }

    @Override
    public boolean existsByVoterAndElection(VoterId voterId, ElectionId electionId) {
        return voteJpaRepository.existsByVoterIdAndElectionId(voterId.value(), electionId.value());
    }
}
