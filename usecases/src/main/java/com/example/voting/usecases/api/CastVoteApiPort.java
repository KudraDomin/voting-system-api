package com.example.voting.usecases.api;

public interface CastVoteApiPort {

    VoteView castVote(CastVoteCommand command);
}
