package com.test.data;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataContainer {
    /**
     * all known airports
     */
    private final List<AirportData> airportData = Collections.synchronizedList(new ArrayList<>());

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics
     */
    private final Map<AirportData, Integer> requestFrequency = Collections.synchronizedMap(new TreeMap<>());

    private final Map<Double, Integer> radiusFreq = Collections.synchronizedMap(new HashMap<>());


    /**
     * A dummy init method that loads hard coded data
     */
    @PostConstruct
    protected void init() {
        airportData.clear();
        requestFrequency.clear();

        addAirport("BOS", 42, -71);
        addAirport("EWR", 40, -74);
        addAirport("JFK", 40, -73);
        addAirport("LGA", 40, -75);
        addAirport("MMU", 40, -76);
    }

    public void reset(){
        init();
    }

    public AirportData addAirport(String iataCode, int latitude, int longitude) {
        AirportData ad = new AirportData(iataCode, latitude, longitude, new AtmosphericInformation());
        airportData.add(ad);
        return ad;
    }

    public void UpdateRequestFrequency(AirportData airportData, Double radius) {
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
    }

    public List<AirportData> getAirportData() {
        return Collections.unmodifiableList(airportData);
    }

    public Map<AirportData, Integer> getRequestFrequency() {
        return Collections.unmodifiableMap(requestFrequency);
    }

    public Map<Double, Integer> getRadiusFreq() {
        return Collections.unmodifiableMap(radiusFreq);
    }
}