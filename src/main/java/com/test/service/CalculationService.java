package com.test.service;

import com.test.model.AirportData;

public interface CalculationService {

    /**
     * calculate distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    double calculateDistance(AirportData ad1, AirportData ad2);
}
