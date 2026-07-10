package com.example.voting.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void given_valid_identifiers_when_cast_then_has_generated_id() {
        // given
        var voterId = VoterId.newId();
        var electionId = ElectionId.newId();
        var optionId = ElectionOptionId.newId();

        // when
        var vote = Vote.cast(voterId, electionId, optionId);

        // then
        assertThat(vote.id()).isNotNull();
    }

    @Test
    void given_valid_identifiers_when_cast_then_references_voter() {
        // given
        var voterId = VoterId.newId();
        var electionId = ElectionId.newId();
        var optionId = ElectionOptionId.newId();

        // when
        var vote = Vote.cast(voterId, electionId, optionId);

        // then
        assertThat(vote.voterId()).isEqualTo(voterId);
    }

    @Test
    void given_null_voter_id_when_construct_then_throws_null_pointer() {
        // given
        var electionId = ElectionId.newId();
        var optionId = ElectionOptionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new Vote(VoteId.newId(), null, electionId, optionId));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_null_election_id_when_construct_then_throws_null_pointer() {
        // given
        var voterId = VoterId.newId();
        var optionId = ElectionOptionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new Vote(VoteId.newId(), voterId, null, optionId));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_null_option_id_when_construct_then_throws_null_pointer() {
        // given
        var voterId = VoterId.newId();
        var electionId = ElectionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new Vote(VoteId.newId(), voterId, electionId, null));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_null_vote_id_when_construct_then_throws_null_pointer() {
        // given
        var voterId = VoterId.newId();
        var electionId = ElectionId.newId();
        var optionId = ElectionOptionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new Vote(null, voterId, electionId, optionId));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }
}
