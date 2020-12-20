package com.test.service;

import com.test.model.PointData;
import com.test.model.PointDataType;
import com.test.exception.WeatherException;

import java.util.Set;

public interface CollectorService {

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link PointDataType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    void addDataPoint(String iataCode, PointDataType pointType, PointData dp) throws WeatherException;

    /**
     * List known airport IATA codes
     * @return list of string
     */
    Set<String> getAirports();
}
