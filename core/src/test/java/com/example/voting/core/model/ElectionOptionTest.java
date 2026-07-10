package com.example.voting.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ElectionOptionTest {

    private static final int MAX_LABEL_LENGTH = 200;

    @Test
    void given_valid_label_when_create_then_has_generated_id() {
        // given
        var electionId = ElectionId.newId();

        // when
        var option = ElectionOption.create(electionId, "Candidate A");

        // then
        assertThat(option.id()).isNotNull();
    }

    @Test
    void given_valid_label_when_create_then_label_is_preserved() {
        // given
        var electionId = ElectionId.newId();

        // when
        var option = ElectionOption.create(electionId, "Candidate A");

        // then
        assertThat(option.label()).isEqualTo("Candidate A");
    }

    @Test
    void given_label_with_surrounding_whitespace_when_create_then_label_is_trimmed() {
        // given
        var electionId = ElectionId.newId();

        // when
        var option = ElectionOption.create(electionId, "  Candidate A  ");

        // then
        assertThat(option.label()).isEqualTo("Candidate A");
    }

    @Test
    void given_null_label_when_create_then_throws_null_pointer() {
        // given
        var electionId = ElectionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> ElectionOption.create(electionId, null));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t"})
    void given_blank_label_when_create_then_throws_illegal_argument(String label) {
        // given
        var electionId = ElectionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> ElectionOption.create(electionId, label));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_label_exceeding_maximum_length_when_create_then_throws_illegal_argument() {
        // given
        var electionId = ElectionId.newId();
        var label = "a".repeat(MAX_LABEL_LENGTH + 1);

        // when
        var thrown = assertThatThrownBy(() -> ElectionOption.create(electionId, label));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_label_at_maximum_length_when_create_then_creates() {
        // given
        var electionId = ElectionId.newId();
        var label = "a".repeat(MAX_LABEL_LENGTH);

        // when
        var option = ElectionOption.create(electionId, label);

        // then
        assertThat(option.label()).hasSize(MAX_LABEL_LENGTH);
    }

    @Test
    void given_null_election_id_when_create_then_throws_null_pointer() {
        // given
        ElectionId electionId = null;

        // when
        var thrown = assertThatThrownBy(() -> ElectionOption.create(electionId, "Candidate A"));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @Test
    void given_null_id_when_construct_then_throws_null_pointer() {
        // given
        var electionId = ElectionId.newId();

        // when
        var thrown = assertThatThrownBy(() -> new ElectionOption(null, electionId, "Candidate A"));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }
}
