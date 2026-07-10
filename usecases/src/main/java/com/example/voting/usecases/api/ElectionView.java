package com.example.voting.usecases.api;

import java.util.List;
import java.util.UUID;

import com.example.voting.core.model.Election;

public record ElectionView(UUID id, String title, List<ElectionOptionView> options) {

    public static ElectionView from(Election election) {
        var optionViews = election.options().stream()
                .map(ElectionOptionView::from)
                .toList();

        return new ElectionView(election.id().value(), election.title(), optionViews);
    }
}
