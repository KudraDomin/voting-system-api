package com.example.voting.usecases.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.voting.core.exception.DuplicateVoteException;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.Vote;
import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.core.service.VoteFactory;
import com.example.voting.usecases.api.CastVoteCommand;
import com.example.voting.usecases.exception.ElectionNotFoundException;
import com.example.voting.usecases.exception.VoterNotFoundException;
import com.example.voting.usecases.spi.ElectionSpiPort;
import com.example.voting.usecases.spi.VoteSpiPort;
import com.example.voting.usecases.spi.VoterSpiPort;

@ExtendWith(MockitoExtension.class)
class CastVoteServiceTest {

    private static final String VOTER_NAME = "Jane Doe";

    private static final String ELECTION_TITLE = "Mayor Election 2025";

    private static final String OPTION_LABEL = "Candidate A";

    @Mock
    private VoterSpiPort voterSpiPort;

    @Mock
    private ElectionSpiPort electionSpiPort;

    @Mock
    private VoteSpiPort voteSpiPort;

    @Mock
    private VoteFactory voteFactory;

    private CastVoteService castVoteService;

    @BeforeEach
    void setUp() {
        castVoteService = new CastVoteService(voterSpiPort, electionSpiPort, voteSpiPort, voteFactory);
    }

    @Test
    void given_eligible_voter_when_cast_vote_then_returns_view_with_option() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var election = Election.create(ELECTION_TITLE).addOption(OPTION_LABEL);
        var optionId = election.options().get(0).id();
        var vote = Vote.cast(voter.id(), election.id(), optionId);

        given(voterSpiPort.findById(voter.id())).willReturn(Optional.of(voter));
        given(electionSpiPort.findById(election.id())).willReturn(Optional.of(election));
        given(voteSpiPort.existsByVoterAndElection(voter.id(), election.id())).willReturn(false);
        given(voteFactory.castVote(voter, election, optionId)).willReturn(vote);
        given(voteSpiPort.save(vote)).willReturn(vote);

        var command = new CastVoteCommand(election.id().value(), voter.id().value(), optionId.value());

        // when
        var view = castVoteService.castVote(command);

        // then
        assertThat(view.optionId()).isEqualTo(optionId.value());
    }

    @Test
    void given_eligible_voter_when_cast_vote_then_saves_produced_vote() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var election = Election.create(ELECTION_TITLE).addOption(OPTION_LABEL);
        var optionId = election.options().get(0).id();
        var vote = Vote.cast(voter.id(), election.id(), optionId);

        given(voterSpiPort.findById(voter.id())).willReturn(Optional.of(voter));
        given(electionSpiPort.findById(election.id())).willReturn(Optional.of(election));
        given(voteSpiPort.existsByVoterAndElection(voter.id(), election.id())).willReturn(false);
        given(voteFactory.castVote(voter, election, optionId)).willReturn(vote);
        given(voteSpiPort.save(vote)).willReturn(vote);

        var command = new CastVoteCommand(election.id().value(), voter.id().value(), optionId.value());

        // when
        castVoteService.castVote(command);

        // then
        then(voteSpiPort).should().save(vote);
    }

    @Test
    void given_missing_voter_when_cast_vote_then_throws_voter_not_found() {
        // given
        var voterId = UUID.randomUUID();
        given(voterSpiPort.findById(VoterId.of(voterId))).willReturn(Optional.empty());

        var command = new CastVoteCommand(UUID.randomUUID(), voterId, UUID.randomUUID());

        // when
        var thrown = assertThatThrownBy(() -> castVoteService.castVote(command));

        // then
        thrown.isInstanceOf(VoterNotFoundException.class);
    }

    @Test
    void given_missing_election_when_cast_vote_then_throws_election_not_found() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var electionId = UUID.randomUUID();
        given(voterSpiPort.findById(voter.id())).willReturn(Optional.of(voter));
        given(electionSpiPort.findById(ElectionId.of(electionId))).willReturn(Optional.empty());

        var command = new CastVoteCommand(electionId, voter.id().value(), UUID.randomUUID());

        // when
        var thrown = assertThatThrownBy(() -> castVoteService.castVote(command));

        // then
        thrown.isInstanceOf(ElectionNotFoundException.class);
    }

    @Test
    void given_voter_already_voted_when_cast_vote_then_throws_duplicate_vote() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var election = Election.create(ELECTION_TITLE).addOption(OPTION_LABEL);
        var optionId = election.options().get(0).id();

        given(voterSpiPort.findById(voter.id())).willReturn(Optional.of(voter));
        given(electionSpiPort.findById(election.id())).willReturn(Optional.of(election));
        given(voteSpiPort.existsByVoterAndElection(voter.id(), election.id())).willReturn(true);

        var command = new CastVoteCommand(election.id().value(), voter.id().value(), optionId.value());

        // when
        var thrown = assertThatThrownBy(() -> castVoteService.castVote(command));

        // then
        thrown.isInstanceOf(DuplicateVoteException.class);
    }

    @Test
    void given_voter_already_voted_when_cast_vote_then_does_not_save_vote() {
        // given
        var voter = Voter.register(VOTER_NAME);
        var election = Election.create(ELECTION_TITLE).addOption(OPTION_LABEL);
        var optionId = election.options().get(0).id();

        given(voterSpiPort.findById(voter.id())).willReturn(Optional.of(voter));
        given(electionSpiPort.findById(election.id())).willReturn(Optional.of(election));
        given(voteSpiPort.existsByVoterAndElection(voter.id(), election.id())).willReturn(true);

        var command = new CastVoteCommand(election.id().value(), voter.id().value(), optionId.value());

        // when
        assertThatThrownBy(() -> castVoteService.castVote(command));

        // then
        then(voteSpiPort).should(never()).save(any(Vote.class));
        then(voteFactory).should(never()).castVote(any(), any(), any());
    }
}
