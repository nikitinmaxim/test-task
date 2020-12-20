package com.test.rest.dto;

import com.test.exception.WeatherException;
import com.test.model.PointData;
import com.test.model.PointDataType;
import com.test.util.Constants;
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

    private Map<PointDataType, PointData> information = new HashMap<>();

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;
}