package com.test.service;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import com.test.data.DataContainer;
import com.test.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private final CalculationService calculationService;
    private final DataContainer dataContainer;

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or Empty if not found
     */
    @Override
    public Optional<AirportData> findAirport(String iataCode) {
        return dataContainer.findAirportData(iataCode);
    }

    @Override
    public AirportData addAirport(String iata, int latitude, int longitude) {
        return dataContainer.addAirport(iata, latitude, longitude);
    }

    @Override
    public Optional<Boolean> deleteAirport(String iata) {
        return dataContainer.deleteAirportData(iata)
                .map(data -> true);
    }

    @Override
    public List<AtmosphericInformation> queryWeather(String iata, double radius) {
        return findAirport(iata)
                .map(airportData -> {
                    updateRequestFrequency(airportData);
                    updateRequestFrequency(radius);
                    return (radius == 0)
                            ? Collections.singletonList(airportData.getAtmosphericInformation())
                            : dataContainer.getAirportData().stream()
                                            .filter(data -> calculationService.calculateDistance(airportData, data) <= radius)
                                            .map(data -> data.getAtmosphericInformation())
                                            .collect(Collectors.toList());
                })
                .orElseGet(() -> Collections.emptyList());
    }
    @Override
    public Map<String, Object> queryPingResponse() {
        Map<String, Object> retval = new HashMap<>();

        long datasize = dataContainer.getAirportData().stream()
                                        .map(airportData -> airportData.getAtmosphericInformation())
                                        .filter(atmosphericInformation -> atmosphericInformation.isFresh())
                                        .count();
        retval.put("datasize", datasize);

        Map<String, Double> iata_freq = dataContainer.getAirportData().stream()
                                                .map(airportData -> {
                                                    Map<AirportData, Integer> qm = dataContainer.getRequestFrequency();
                                                    double fr = (double) qm.getOrDefault(airportData, 0) / qm.size();
                                                    return Pair.of(airportData.getIata(), fr);
                                                })
                                                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        retval.put("iata_freq", iata_freq);

        OptionalDouble best = dataContainer.getRadiusFreq().keySet().stream()
                            .mapToDouble(d -> d)
                            .max();

        int m =  (int) (best.isPresent() ? best.getAsDouble()  : 1000);
        m = m + 1;
        int[] h = new int[m];
        for (Map.Entry<Double, Integer> e : dataContainer.getRadiusFreq().entrySet()) {
            int i = e.getKey().intValue() % 10;
            h[i] += e.getValue();
        }

        retval.put("radius_freq", h);
        return retval;
    }

    /**
     * Records information about how often requests are made
     *
     * @param data   an airport data
     */
    private void updateRequestFrequency(AirportData data) {
        dataContainer.updateRequestFrequency(data);
    }
    /**
     * Records information about how often requests are made
     *
     * @param radius query radius
     */
    private void updateRequestFrequency(Double radius) {
        dataContainer.updateRequestFrequency(radius);
    }

}
