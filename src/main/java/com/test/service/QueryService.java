package com.test.service;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import com.test.data.DataContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final CalculationService calculationService;
    private final DataContainer dataContainer;

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public Optional<AirportData> findAirportData(String iataCode) {
        return dataContainer.getAirportData().stream()
                .filter(ap -> ap.getIata().equals(iataCode))
                .findFirst();
    }

    public List<AtmosphericInformation> queryWeather(String iata, double radius) {
        return findAirportData(iata)
                .map(airportData -> {
                    updateRequestFrequency(airportData, radius);
                    return (radius == 0)
                            ? Collections.singletonList(airportData.getAtmosphericInformation())
                            : dataContainer.getAirportData().stream()
                                            .filter(data -> calculationService.calculateDistance(airportData, data) <= radius)
                                            .map(data -> data.getAtmosphericInformation())
                                            .collect(Collectors.toList());
                })
                .orElseGet(() -> Collections.emptyList());
    }

    public Map<String, Object> queryPingResponse() {
        Map<String, Object> retval = new HashMap<>();
        long datasize = 0L;
        for (AirportData a : dataContainer.getAirportData()) {
            AtmosphericInformation info = a.getAtmosphericInformation();
            if (info.isFresh()) {
                datasize++;
            }
        }
        retval.put("datasize", datasize);

        Map<String, Double> f = new HashMap<>();
        // fraction of queries
        for (AirportData data : dataContainer.getAirportData()) {
            Map<AirportData, Integer> qm = dataContainer.getRequestFrequency();
            double fr = (double) qm.getOrDefault(data, 0) / qm.size();
            f.put(data.getIata(), fr);
        }
        retval.put("iata_freq", f);

        boolean seen = false;
        Double best = null;
        for (Double aDouble : dataContainer.getRadiusFreq().keySet()) {
            if (!seen || Double.compare(aDouble, best) > 0) {
                seen = true;
                best = aDouble;
            }
        }
        int m = Integer.valueOf(seen ? best.intValue() : 1000) + 1;

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
     * @param iata   an iata code
     * @param radius query radius
     */
    private void updateRequestFrequency(String iata, Double radius) {
        findAirportData(iata)
                .ifPresent(airportData -> updateRequestFrequency(airportData, radius));
    }

    /**
     * Records information about how often requests are made
     *
     * @param data   an airport data
     * @param radius query radius
     */
    private void updateRequestFrequency(AirportData data, Double radius) {
        dataContainer.UpdateRequestFrequency(data, radius);
    }

}
