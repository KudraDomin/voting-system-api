package com.example.voting.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class IdentifiersTest {

    @Test
    void given_uuid_when_voter_id_of_then_value_is_preserved() {
        // given
        var value = UUID.randomUUID();

        // when
        var id = VoterId.of(value);

        // then
        assertThat(id.value()).isEqualTo(value);
    }

    @Test
    void given_null_when_voter_id_of_then_throws_null_pointer() {
        // given
        UUID value = null;

        // when
        var thrown = assertThatThrownBy(() -> VoterId.of(value));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_new_voter_ids_when_generated_then_they_are_distinct() {
        // given
        var first = VoterId.newId();

        // when
        var second = VoterId.newId();

        // then
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void given_uuid_when_election_id_of_then_value_is_preserved() {
        // given
        var value = UUID.randomUUID();

        // when
        var id = ElectionId.of(value);

        // then
        assertThat(id.value()).isEqualTo(value);
    }

    @Test
    void given_null_when_election_id_of_then_throws_null_pointer() {
        // given
        UUID value = null;

        // when
        var thrown = assertThatThrownBy(() -> ElectionId.of(value));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_uuid_when_election_option_id_of_then_value_is_preserved() {
        // given
        var value = UUID.randomUUID();

        // when
        var id = ElectionOptionId.of(value);

        // then
        assertThat(id.value()).isEqualTo(value);
    }

    @Test
    void given_null_when_election_option_id_of_then_throws_null_pointer() {
        // given
        UUID value = null;

        // when
        var thrown = assertThatThrownBy(() -> ElectionOptionId.of(value));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_uuid_when_vote_id_of_then_value_is_preserved() {
        // given
        var value = UUID.randomUUID();

        // when
        var id = VoteId.of(value);

        // then
        assertThat(id.value()).isEqualTo(value);
    }

    @Test
    void given_null_when_vote_id_of_then_throws_null_pointer() {
        // given
        UUID value = null;

        // when
        var thrown = assertThatThrownBy(() -> VoteId.of(value));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_new_vote_ids_when_generated_then_they_are_distinct() {
        // given
        var first = VoteId.newId();

        // when
        var second = VoteId.newId();

        // then
        assertThat(first).isNotEqualTo(second);
    }
}
