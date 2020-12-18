package com.test.model;

import java.util.Objects;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData implements Comparable<AirportData>{

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
    public int compareTo(AirportData o) {
        return iata==null?-1:iata.compareTo(o.iata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportData that = (AirportData) o;
        return Objects.equals(iata, that.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata);
    }
}
