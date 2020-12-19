package com.test.model;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData implements Comparable<AirportData> {

    /** the three letter IATA code */
    public String iata;

    /** latitude value in degrees */
    public int latitude;

    /** longitude value in degrees */
    public int longitude;

    public AtmosphericInformation atmosphericInformation;

    public AirportData(String iata, int latitude, int longitude, AtmosphericInformation atmosphericInformation) {
        this.iata = iata;
        this.latitude = latitude;
        this.longitude = longitude;
        this.atmosphericInformation = atmosphericInformation;
    }

    @Override
    public int compareTo(AirportData airportData) {
        return iata.compareTo(airportData.iata);
    }
}
