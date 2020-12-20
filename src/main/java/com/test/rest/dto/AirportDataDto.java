package com.test.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDataDto implements Comparable<AirportDataDto> {

    /**
     * the three letter IATA code
     */
    private String iata;

    /**
     * latitude value in degrees
     */
    private int latitude;

    /**
     * longitude value in degrees
     */
    private int longitude;

    private AtmosphericInformationDto atmosphericInformation;

    @Override
    public int compareTo(AirportDataDto airportData) {
        return iata.compareTo(airportData.iata);
    }
}
