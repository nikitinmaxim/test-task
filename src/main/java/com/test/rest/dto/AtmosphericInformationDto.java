package com.test.rest.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * encapsulates sensor information for a particular location
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmosphericInformationDto {

    private Map<PointDataTypeDto, PointDataDto> information = new HashMap<>();

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;
}