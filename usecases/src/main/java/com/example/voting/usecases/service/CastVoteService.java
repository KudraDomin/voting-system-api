package com.example.voting.usecases.service;

import java.util.UUID;

import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.ElectionOptionId;
import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.service.VoteFactory;
import com.example.voting.usecases.api.CastVoteApiPort;
import com.example.voting.usecases.api.CastVoteCommand;
import com.example.voting.usecases.api.VoteView;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;
import com.example.voting.usecases.spi.VoteSpiPort;
import com.example.voting.usecases.spi.VoterSpiPort;

public class CastVoteService implements CastVoteApiPort {

    private final VoterSpiPort voterSpiPort;

    private final ElectionSpiPort electionSpiPort;

    private final VoteSpiPort voteSpiPort;

    private final VoteFactory voteFactory;

    public CastVoteService(
            VoterSpiPort voterSpiPort,
            ElectionSpiPort electionSpiPort,
            VoteSpiPort voteSpiPort,
            VoteFactory voteFactory) {
        this.voterSpiPort = voterSpiPort;
        this.electionSpiPort = electionSpiPort;
        this.voteSpiPort = voteSpiPort;
        this.voteFactory = voteFactory;
    }

    @Override
    public VoteView castVote(CastVoteCommand command) {
        var voter = findVoter(command.voterId());
        var election = findElection(command.electionId());
        var optionId = ElectionOptionId.of(command.optionId());

        requireVoterHasNotVoted(voter, election);

        var vote = voteFactory.castVote(voter, election, optionId);

        return VoteView.from(voteSpiPort.save(vote));
    }

    private Voter findVoter(UUID voterId) {
        return voterSpiPort.findById(VoterId.of(voterId))
                .orElseThrow(() -> new VoterNotFoundException(voterId));
    }

    private Election findElection(UUID electionId) {
        return electionSpiPort.findById(ElectionId.of(electionId))
                .orElseThrow(() -> new ElectionNotFoundException(electionId));
    }

    private void requireVoterHasNotVoted(Voter voter, Election election) {
        if (voteSpiPort.existsByVoterAndElection(voter.id(), election.id())) {
            throw new DuplicateVoteException(voter.id(), election.id());
        }
    }
}
