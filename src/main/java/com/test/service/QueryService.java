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
    Optional<AirportData> findAirport(String iataCode);

    /**
     *  Add airport data
     *
     * @param iataCode as a string
     * @param latitude coordinate of airport
     * @param longitude coordinate of airport
     * @return created airport data or exists if found
     */
    AirportData addAirport(String iataCode, int latitude, int longitude);

    /**
     * Delete airport data
     *
     * @param iataCode as a string
     * @return true if deleted or false if not found
     */
    Optional<Boolean> deleteAirport(String iataCode);

    /**
     * Query a atmospheric information
     *
     * @param iataCode as a string
     * @param radius radius in km
     * @return list of atmospheric information
     */
    List<AtmosphericInformation> queryWeather(String iataCode, double radius);

    Map<String, Object> queryPingResponse();
}
