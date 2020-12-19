package com.test.model;

import com.test.exception.WeatherException;
import com.test.util.Constants;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * encapsulates sensor information for a particular location
 */
@Getter
public class AtmosphericInformation {

    private final Map<DataPointType, DataPoint> information = new HashMap<>();

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;

    public AtmosphericInformation() {

    }

    public void updateContents(DataPointType pointType, DataPoint dataPoint) throws WeatherException {
        lastUpdateTime = System.currentTimeMillis();
        information.put(pointType, dataPoint);
    }

    public boolean isFresh(){
        return Math.abs(lastUpdateTime - System.currentTimeMillis()) < Constants.FRESH_PERIOD;
    }

}