package com.test.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@RequiredArgsConstructor(staticName = "of")
@Valid
public class ExternalAirportData {
    /**
     * Main city served by airport. May be spelled differently from name.
     */
    @NotBlank(message = "City must be not blank")
    private final String city;

    /**
     * Country or territory where airport is located.
     */
    @NotBlank(message = "Country must be not blank")
    private final String country;

    /**
     * 3-letter FAA code or IATA code (blank or "" if not assigned)
     */
    @Size(max = 3, message = "IATA code must be 3 letter size")
    @NotNull(message = "IATA code can not be null")
    private final String iata;

    /**
     * 4-letter ICAO code (blank or "" if not assigned)
     */
    @Size(max = 4, message = "ICAO code must be 4 letter size")
    @NotNull(message = "ICAO code must be not null")
    private final String icao;
    /**
     * Decimal degrees, up to 6 significant digits. Negative is South, positive is North.
     */
    @Max(value = 90, message = "Illegal value for latitude")
    @Min(value = -90, message = "Illegal value for latitude")
    private final double latitude;
    /**
     * Decimal degrees, up to 6 significant digits. Negative is West, positive is East.
     */
    @Max(value = 180, message = "Illegal value for longitude")
    @Min(value = -180, message = "Illegal value for longitude")
    private final double longitude;
    /**
     * In feet
     */
    private final int altitude;
    /**
     * Hours offset from UTC. Fractional hours are expressed as decimals. (e.g. India is 5.5)
     */
    @Max(value = 24, message = "Illegal value for hours offset from UTC")
    @Min(value = 0, message = "Illegal value for hours offset from UTC")
    private final double tzOffset;
    /**
     * One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown)
     */
    @NotNull(message = "Illegal value for DST")
    private final DST dst;
}
