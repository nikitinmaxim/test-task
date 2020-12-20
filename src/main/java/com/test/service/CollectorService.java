package com.test.service;

import com.test.model.PointData;
import com.test.model.PointDataType;
import com.test.exception.WeatherException;

import java.util.Set;

public interface CollectorService {

    void addDataPoint(String iataCode, PointDataType pointType, PointData dp) throws WeatherException;

    Set<String> getAirports();
}
