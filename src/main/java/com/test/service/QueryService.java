package com.test.service;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;

import java.util.*;

public interface QueryService {

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or Empty if not found
     */
    Optional<AirportData> findAirportData(String iataCode);

    AirportData addAirport(String iata, int latitude, int longitude);

    Optional<Boolean> deleteAirportData(String iata);

    List<AtmosphericInformation> queryWeather(String iata, double radius);

    Map<String, Object> queryPingResponse();
}
