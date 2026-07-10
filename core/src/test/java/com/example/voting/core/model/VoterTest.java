package com.example.voting.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.voting.core.exception.VoterAlreadyBlockedException;
import com.example.voting.core.exception.VoterNotBlockedException;

class VoterTest {

    private static final int MAX_NAME_LENGTH = 120;

    @Test
    void given_valid_name_when_register_then_voter_is_active() {
        // given
        var name = "Jane Doe";

        // when
        var voter = Voter.register(name);

        // then
        assertThat(voter.isActive()).isTrue();
    }

    @Test
    void given_valid_name_when_register_then_status_is_active() {
        // given
        var name = "Jane Doe";

        // when
        var voter = Voter.register(name);

        // then
        assertThat(voter.status()).isEqualTo(VoterStatus.ACTIVE);
    }

    @Test
    void given_active_voter_when_block_then_status_is_blocked() {
        // given
        var voter = Voter.register("Jane Doe");

        // when
        var blocked = voter.block();

        // then
        assertThat(blocked.status()).isEqualTo(VoterStatus.BLOCKED);
    }

    @Test
    void given_active_voter_when_block_then_reports_blocked() {
        // given
        var voter = Voter.register("Jane Doe");

        // when
        var blocked = voter.block();

        // then
        assertThat(blocked.isBlocked()).isTrue();
    }

    @Test
    void given_blocked_voter_when_block_then_throws_voter_already_blocked() {
        // given
        var voter = Voter.register("Jane Doe").block();

        // when
        var thrown = assertThatThrownBy(voter::block);

        // then
        thrown.isInstanceOf(VoterAlreadyBlockedException.class);
    }

    @Test
    void given_blocked_voter_when_unblock_then_status_is_active() {
        // given
        var voter = Voter.register("Jane Doe").block();

        // when
        var unblocked = voter.unblock();

        // then
        assertThat(unblocked.status()).isEqualTo(VoterStatus.ACTIVE);
    }

    @Test
    void given_active_voter_when_unblock_then_throws_voter_not_blocked() {
        // given
        var voter = Voter.register("Jane Doe");

        // when
        var thrown = assertThatThrownBy(voter::unblock);

        // then
        thrown.isInstanceOf(VoterNotBlockedException.class);
    }

    @Test
    void given_active_voter_when_block_then_keeps_same_id() {
        // given
        var voter = Voter.register("Jane Doe");

        // when
        var blocked = voter.block();

        // then
        assertThat(blocked.id()).isEqualTo(voter.id());
    }

    @Test
    void given_null_name_when_register_then_throws_null_pointer() {
        // given
        String name = null;

        // when
        var thrown = assertThatThrownBy(() -> Voter.register(name));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    void given_blank_name_when_register_then_throws_illegal_argument(String name) {
        // given
        var blankName = name;

        // when
        var thrown = assertThatThrownBy(() -> Voter.register(blankName));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_name_exceeding_maximum_length_when_register_then_throws_illegal_argument() {
        // given
        var name = "a".repeat(MAX_NAME_LENGTH + 1);

        // when
        var thrown = assertThatThrownBy(() -> Voter.register(name));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_name_at_maximum_length_when_register_then_registers() {
        // given
        var name = "a".repeat(MAX_NAME_LENGTH);

        // when
        var voter = Voter.register(name);

        // then
        assertThat(voter.name()).hasSize(MAX_NAME_LENGTH);
    }

    @Test
    void given_name_with_surrounding_whitespace_when_register_then_name_is_trimmed() {
        // given
        var name = "  Jane Doe  ";

        // when
        var voter = Voter.register(name);

        // then
        assertThat(voter.name()).isEqualTo("Jane Doe");
    }

    @Test
    void given_null_id_when_construct_then_throws_null_pointer() {
        // given
        VoterId id = null;

        // when
        var thrown = assertThatThrownBy(() -> new Voter(id, "Jane Doe", VoterStatus.ACTIVE));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_null_status_when_construct_then_throws_null_pointer() {
        // given
        var id = VoterId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new Voter(id, "Jane Doe", null));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_two_voters_with_same_id_when_compared_then_they_are_equal() {
        // given
        var id = VoterId.newId();

        // when
        var first = new Voter(id, "Jane Doe", VoterStatus.ACTIVE);
        var second = new Voter(id, "Different Name", VoterStatus.BLOCKED);

        // then
        assertThat(first).isEqualTo(second);
    }

    @Test
    void given_two_voters_with_different_ids_when_compared_then_they_are_not_equal() {
        // given
        var first = Voter.register("Jane Doe");

        // when
        var second = Voter.register("Jane Doe");

        // then
        assertThat(first).isNotEqualTo(second);
    }
}
