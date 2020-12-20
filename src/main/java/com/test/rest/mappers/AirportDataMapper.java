package com.test.rest.mappers;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import com.test.rest.dto.AirportDataDto;
import com.test.rest.dto.AtmosphericInformationDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirportDataMapper {
    AirportData toEntity(AirportDataDto dto);
    AirportDataDto toDto(AirportData entity);

    AtmosphericInformation toEntity (AtmosphericInformationDto dto);
    AtmosphericInformationDto toDto(AtmosphericInformation entity);
    List<AtmosphericInformationDto> toDto(List<AtmosphericInformation> entity);
}
