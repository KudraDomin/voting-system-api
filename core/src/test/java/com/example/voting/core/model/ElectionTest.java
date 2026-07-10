package com.example.voting.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ElectionTest {

    private static final int MAX_TITLE_LENGTH = 200;

    @Test
    void given_valid_title_when_create_then_has_no_options() {
        // given
        var title = "Mayor Election 2025";

        // when
        var election = Election.create(title);

        // then
        assertThat(election.options()).isEmpty();
    }

    @Test
    void given_valid_title_when_create_then_title_is_preserved() {
        // given
        var title = "Mayor Election 2025";

        // when
        var election = Election.create(title);

        // then
        assertThat(election.title()).isEqualTo(title);
    }

    @Test
    void given_election_when_add_option_then_returns_instance_with_option() {
        // given
        var election = Election.create("Mayor Election 2025");

        // when
        var updated = election.addOption("Candidate A");

        // then
        assertThat(updated.options()).hasSize(1);
    }

    @Test
    void given_election_when_add_option_then_option_label_is_preserved() {
        // given
        var election = Election.create("Mayor Election 2025");

        // when
        var updated = election.addOption("Candidate A");

        // then
        assertThat(updated.options().get(0).label()).isEqualTo("Candidate A");
    }

    @Test
    void given_election_when_add_option_then_original_instance_is_unchanged() {
        // given
        var election = Election.create("Mayor Election 2025");

        // when
        election.addOption("Candidate A");

        // then
        assertThat(election.options()).isEmpty();
    }

    @Test
    void given_election_with_option_when_has_option_then_returns_true() {
        // given
        var election = Election.create("Mayor Election 2025").addOption("Candidate A");
        var optionId = election.options().get(0).id();

        // when
        var result = election.hasOption(optionId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void given_election_without_option_when_has_option_then_returns_false() {
        // given
        var election = Election.create("Mayor Election 2025");
        var unknownOptionId = ElectionOptionId.newId();

        // when
        var result = election.hasOption(unknownOptionId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void given_election_when_options_accessed_then_collection_is_immutable() {
        // given
        var election = Election.create("Mayor Election 2025").addOption("Candidate A");
        var newOption = ElectionOption.create(election.id(), "Candidate B");

        // when
        var thrown = assertThatThrownBy(() -> election.options().add(newOption));

        // then
        thrown.isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void given_null_title_when_create_then_throws_null_pointer() {
        // given
        String title = null;

        // when
        var thrown = assertThatThrownBy(() -> Election.create(title));

        // then
        thrown.isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t"})
    void given_blank_title_when_create_then_throws_illegal_argument(String title) {
        // given
        var blankTitle = title;

        // when
        var thrown = assertThatThrownBy(() -> Election.create(blankTitle));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_title_exceeding_maximum_length_when_create_then_throws_illegal_argument() {
        // given
        var title = "a".repeat(MAX_TITLE_LENGTH + 1);

        // when
        var thrown = assertThatThrownBy(() -> Election.create(title));

        // then
        thrown.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_title_at_maximum_length_when_create_then_creates() {
        // given
        var title = "a".repeat(MAX_TITLE_LENGTH);

        // when
        var election = Election.create(title);

        // then
        assertThat(election.title()).hasSize(MAX_TITLE_LENGTH);
    }

    @Test
    void given_election_when_add_option_then_option_belongs_to_election() {
        // given
        var election = Election.create("Mayor Election 2025");

        // when
        var updated = election.addOption("Candidate A");

        // then
        assertThat(updated.options().get(0).electionId()).isEqualTo(election.id());
    }
}
