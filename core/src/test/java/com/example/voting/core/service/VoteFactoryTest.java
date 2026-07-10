package com.example.voting.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.voting.core.exception.BlockedVoterCannotVoteException;
import com.example.voting.core.exception.OptionNotInElectionException;
import com.example.voting.core.model.Election;
import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.ElectionOption;
import com.example.voting.core.model.Voter;
import com.example.voting.core.model.VoterId;
import com.example.voting.core.model.VoterStatus;

class VoteFactoryTest {

    private VoteFactory voteFactory;

    @BeforeEach
    void setUp() {
        voteFactory = new VoteFactory();
    }

    @Test
    void given_active_voter_and_valid_option_when_cast_vote_then_returns_vote() {
        // given
        var voter = Voter.register("Jane Doe");
        var election = Election.create("Mayor Election 2025").addOption("Candidate A");
        var optionId = election.options().get(0).id();

        // when
        var vote = voteFactory.castVote(voter, election, optionId);

        // then
        assertThat(vote.optionId()).isEqualTo(optionId);
    }

    @Test
    void given_active_voter_and_valid_option_when_cast_vote_then_vote_references_voter() {
        // given
        var voter = Voter.register("Jane Doe");
        var election = Election.create("Mayor Election 2025").addOption("Candidate A");
        var optionId = election.options().get(0).id();

        // when
        var vote = voteFactory.castVote(voter, election, optionId);

        // then
        assertThat(vote.voterId()).isEqualTo(voter.id());
    }

    @Test
    void given_blocked_voter_when_cast_vote_then_throws_blocked_voter_cannot_vote() {
        // given
        var voter = new Voter(VoterId.newId(), "Jane Doe", VoterStatus.BLOCKED);
        var election = Election.create("Mayor Election 2025").addOption("Candidate A");
        var optionId = election.options().get(0).id();

        // when
        var thrown = assertThatThrownBy(() -> voteFactory.castVote(voter, election, optionId));

        // then
        thrown.isInstanceOf(BlockedVoterCannotVoteException.class);
    }

    @Test
    void given_option_of_other_election_when_cast_vote_then_throws_option_not_in_election() {
        // given
        var voter = Voter.register("Jane Doe");
        var otherOption = ElectionOption.create(ElectionId.newId(), "Foreign Candidate");
        var election = new Election(ElectionId.newId(), "Mayor Election 2025", List.of());

        // when
        var thrown = assertThatThrownBy(() -> voteFactory.castVote(voter, election, otherOption.id()));

        // then
        thrown.isInstanceOf(OptionNotInElectionException.class);
    }
}
