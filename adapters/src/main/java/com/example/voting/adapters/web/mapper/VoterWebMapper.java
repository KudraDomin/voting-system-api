package com.example.voting.adapters.web.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.web.dto.RegisterVoterRequest;
import com.example.voting.adapters.web.dto.VoterResponse;
import com.example.voting.usecases.api.RegisterVoterCommand;
import com.example.voting.usecases.api.VoterView;

@Mapper(componentModel = "spring")
public interface VoterWebMapper {

    RegisterVoterCommand toCommand(RegisterVoterRequest request);

    VoterResponse toResponse(VoterView view);
}
