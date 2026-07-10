package com.example.voting.usecases.api;

import java.util.UUID;

import com.example.voting.core.model.ElectionOption;

public record ElectionOptionView(UUID id, String label) {

    public static ElectionOptionView from(ElectionOption option) {
        return new ElectionOptionView(option.id().value(), option.label());
    }
}
