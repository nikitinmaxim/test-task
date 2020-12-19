package com.test.service;

import com.test.model.AirportData;
import org.springframework.stereotype.Service;

@Service
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
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double haversine = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                    * Math.cos(Math.toRadians(ad1.getLatitude())) * Math.cos(Math.toRadians(ad2.getLatitude()));
        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(haversine));
    }
}