package com.example.divinationservice.mapper;

import com.example.divinationservice.dto.DivinationProcessDTO;
import com.example.divinationservice.model.DivinationProcess;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DivinationProcessMapper {
    DivinationProcessMapper INSTANCE = Mappers.getMapper(DivinationProcessMapper.class);

    DivinationProcessDTO toDto(DivinationProcess entity);

    DivinationProcess toEntity(DivinationProcessDTO dto);
}