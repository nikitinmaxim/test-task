package com.test.service;

import com.test.model.DataPoint;
import com.test.model.DataPointType;
import com.test.data.DataContainer;
import com.test.exception.WeatherException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {

    private final QueryService queryService;
    private final DataContainer dataContainer;

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    @Override
    public void addDataPoint(String iataCode, DataPointType pointType, DataPoint dp) throws WeatherException {
        queryService.findAirport(iataCode)
                .ifPresent(airportData -> airportData.getAtmosphericInformation().updateContents(pointType, dp));
    }

    @Override
    public Set<String> getAirports() {
        return dataContainer.getAirportData().stream().map(a -> a.getIata()).collect(Collectors.toSet());
    }

}