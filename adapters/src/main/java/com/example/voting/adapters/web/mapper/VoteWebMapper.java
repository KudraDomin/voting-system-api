package com.example.voting.adapters.web.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.web.dto.VoteResponse;
import com.example.voting.usecases.api.VoteView;

@Mapper(componentModel = "spring")
public interface VoteWebMapper {

    VoteResponse toResponse(VoteView view);
}
