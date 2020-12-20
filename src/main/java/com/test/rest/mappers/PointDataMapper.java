package com.test.rest.mappers;

import com.test.model.PointData;
import com.test.model.PointDataType;
import com.test.rest.dto.PointDataDto;
import com.test.rest.dto.PointDataTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PointDataMapper {

    PointData toEntity(PointDataDto dto);
    PointDataDto toDto(PointData entity);

    PointDataType toEntity(PointDataTypeDto dto);
    PointDataTypeDto toDto(PointDataType entity);
    default PointDataType toEntity(String dto) {
        return PointDataType.valueOf(dto.toUpperCase());
    }

}
