package com.test.service;

import com.test.model.DataPoint;
import com.test.model.DataPointType;
import com.test.exception.WeatherException;

import java.util.Set;

public interface CollectorService {

    void addDataPoint(String iataCode, DataPointType pointType, DataPoint dp) throws WeatherException;

    Set<String> getAirports();
}
