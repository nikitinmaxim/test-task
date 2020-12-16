package com.test.service;

import com.test.model.AirportData;

public class CalculationServiceImpl implements CalculationService {
    private static final double EARTH_RADIUS = 6372.8;

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    @Override
    public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.latitude - ad1.latitude);
        double deltaLon = Math.toRadians(ad2.longitude - ad1.longitude);
        double haversine = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                    * Math.cos(Math.toRadians(ad1.latitude)) * Math.cos(Math.toRadians(ad2.latitude));
        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(haversine));
    }
}