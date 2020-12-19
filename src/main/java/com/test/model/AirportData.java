package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
@Data
@AllArgsConstructor
public class AirportData implements Comparable<AirportData> {

    /** the three letter IATA code */
    private String iata;

    /** latitude value in degrees */
    private int latitude;

    /** longitude value in degrees */
    private int longitude;

    private AtmosphericInformation atmosphericInformation;

    @Override
    public int compareTo(AirportData airportData) {
        return iata.compareTo(airportData.iata);
    }
}
