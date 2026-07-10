package com.example.voting.adapters.web.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.web.dto.CreateElectionRequest;
import com.example.voting.adapters.web.dto.ElectionOptionResponse;
import com.example.voting.adapters.web.dto.ElectionResponse;
import com.example.voting.usecases.api.CreateElectionCommand;
import com.example.voting.usecases.api.ElectionOptionView;
import com.example.voting.usecases.api.ElectionView;

@Mapper(componentModel = "spring")
public interface ElectionWebMapper {

    CreateElectionCommand toCommand(CreateElectionRequest request);

    ElectionResponse toResponse(ElectionView view);

    ElectionOptionResponse toResponse(ElectionOptionView view);
}
