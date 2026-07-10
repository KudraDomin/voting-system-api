package com.example.voting.core.exception;

import com.example.voting.core.model.ElectionId;
import com.example.voting.core.model.ElectionOptionId;

public class OptionNotInElectionException extends RuntimeException {

    public OptionNotInElectionException(ElectionId electionId, ElectionOptionId optionId) {
        super("Option %s does not belong to election %s".formatted(optionId.value(), electionId.value()));
    }
}
