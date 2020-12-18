package com.test.service;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import com.test.data.DataContainer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryService {

    @Inject
    private CalculationService calculationService;

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public AirportData findAirportData(String iataCode) {
        return DataContainer.getAirportData().stream()
                .filter(ap -> ap.iata.equals(iataCode))
                .findFirst().orElse(null);
    }

    public List<AtmosphericInformation> queryWeather(String iata, double radius) {
        updateRequestFrequency(iata, radius);

        AirportData airportData = findAirportData(iata);
        List<AtmosphericInformation> retval;
        if (radius == 0) {
            retval = Collections.singletonList(airportData.atmosphericInformation);
        } else {
            List<AtmosphericInformation> list = new ArrayList<>();
            for (AirportData data : DataContainer.getAirportData()) {
                if (calculationService.calculateDistance(airportData, data) <= radius) {
                    AtmosphericInformation atmosphericInformation = data.atmosphericInformation;
                    list.add(atmosphericInformation);
                }
            }
            retval = list;
        }
        return retval;
    }

    public Map<String, Object> queryPingResponse() {
        Map<String, Object> retval = new HashMap<>();
        long datasize = 0L;
        for (AirportData a : DataContainer.getAirportData()) {
            AtmosphericInformation info = a.atmosphericInformation;
            if (info.isFresh()) {
                datasize++;
            }
        }
        retval.put("datasize", datasize);

        Map<String, Double> f = new HashMap<>();
        // fraction of queries
        for (AirportData data : DataContainer.getAirportData()) {
            Map<AirportData, Integer> qm = DataContainer.getRequestFrequency();
            double fr = (double) qm.getOrDefault(data, 0) / qm.size();
            f.put(data.iata, fr);
        }
        retval.put("iata_freq", f);

        boolean seen = false;
        Double best = null;
        for (Double aDouble : DataContainer.getRadiusFreq().keySet()) {
            if (!seen || Double.compare(aDouble, best) > 0) {
                seen = true;
                best = aDouble;
            }
        }
        int m = (seen ? best.intValue() : 1000) + 1;

        int[] h = new int[m];
        for (Map.Entry<Double, Integer> e : DataContainer.getRadiusFreq().entrySet()) {
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
        AirportData airportData = findAirportData(iata);
        DataContainer.UpdateReqeustFreqeuncy(airportData, radius);
    }
}
