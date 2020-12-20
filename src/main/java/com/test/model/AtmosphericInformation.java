package com.test.model;

import com.test.exception.WeatherException;
import com.test.util.Constants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * encapsulates sensor information for a particular location
 */
@Data
public class AtmosphericInformation {

    private final Map<PointDataType, PointData> information = new HashMap<>();

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;

    public AtmosphericInformation() {

    }

    public void updateContents(PointDataType pointType, PointData pointData) throws WeatherException {
        if(pointType != pointData.getType()) {
            throw new WeatherException("Data error pointType not equal pointData type");
        }
        lastUpdateTime = System.currentTimeMillis();
        information.put(pointType, pointData);
    }

    public boolean isFresh(){
        return Math.abs(lastUpdateTime - System.currentTimeMillis()) < Constants.FRESH_PERIOD;
    }

}