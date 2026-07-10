package com.example.voting.adapters.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.example.voting.adapters.persistence.jpa.entity.ElectionOptionEntity;
import com.example.voting.core.model.ElectionOption;

@Mapper(componentModel = "spring")
public interface ElectionOptionEntityMapper extends IdConversions {

    ElectionOptionEntity toEntity(ElectionOption option);

    ElectionOption toDomain(ElectionOptionEntity entity);
}
