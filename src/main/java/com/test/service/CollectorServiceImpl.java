package com.test.service;

import com.test.model.PointData;
import com.test.model.PointDataType;
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

    @Override
    public void addDataPoint(String iataCode, PointDataType pointType, PointData dp) throws WeatherException {
        queryService.findAirport(iataCode)
                .map(airportData -> {
                    airportData.getAtmosphericInformation().updateContents(pointType, dp);
                    return true;
                })
                .orElseThrow(() -> new WeatherException("unknown IATA code: " + iataCode));
    }

    @Override
    public Set<String> getAirports() {
        return dataContainer.getAirportData().stream().map(a -> a.getIata()).collect(Collectors.toSet());
    }

}