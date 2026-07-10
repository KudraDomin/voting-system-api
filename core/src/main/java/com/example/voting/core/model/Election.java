package com.example.voting.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Election {

    private static final int MAX_TITLE_LENGTH = 200;

    private final ElectionId id;

    private final String title;

    private final List<ElectionOption> options;

    public Election(ElectionId id, String title, List<ElectionOption> options) {
        this.id = Objects.requireNonNull(id, "election id must not be null");
        this.title = requireValidTitle(title);
        this.options = List.copyOf(Objects.requireNonNull(options, "options must not be null"));
    }

    public static Election create(String title) {
        return new Election(ElectionId.newId(), title, List.of());
    }

    public Election addOption(String label) {
        var newOption = ElectionOption.create(id, label);

        var extendedOptions = new ArrayList<>(options);
        extendedOptions.add(newOption);

        return new Election(id, title, extendedOptions);
    }

    public boolean hasOption(ElectionOptionId optionId) {
        return options.stream()
                .anyMatch(option -> option.id().equals(optionId));
    }

    public ElectionId id() {
        return id;
    }

    public String title() {
        return title;
    }

    public List<ElectionOption> options() {
        return options;
    }

    private static String requireValidTitle(String title) {
        Objects.requireNonNull(title, "election title must not be null");

        var trimmed = title.strip();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("election title must not be blank");
        }

        if (trimmed.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("election title exceeds maximum length");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Election election)) {
            return false;
        }

        return id.equals(election.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
