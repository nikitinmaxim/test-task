package com.test.data;

import com.test.model.AirportData;
import com.test.model.AtmosphericInformation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataContainer {

    /**
     * all known airports
     */
    private final List<AirportData> airportData = Collections.synchronizedList(new ArrayList<>());

    /**
     * Request frequency per airport
     */
    private final Map<AirportData, Integer> requestFrequency = Collections.synchronizedMap(new TreeMap<>());

    /**
     * Request frequency per radius from airport
     */
    private final Map<Double, Integer> radiusFreq = Collections.synchronizedMap(new HashMap<>());

    /**
     * component init method that loads hard coded data
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

    /**
     * Add airport to the container
     * @param iataCode an IATA code of the airport
     * @param latitude of the airport
     * @param longitude of the airport
     * @return
     */
    public AirportData addAirport(String iataCode, int latitude, int longitude) {
        return findAirportData(iataCode)
                .orElseGet(() -> {
                    AirportData ad = new AirportData(iataCode, latitude, longitude, new AtmosphericInformation());
                    airportData.add(ad);
                    return ad;
                });
    }

    public void updateRequestFrequency(AirportData airportData) {
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
    }

    public void updateRequestFrequency(Double radius) {
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
    }

    public List<AirportData> getAirportData() {
        return Collections.unmodifiableList(airportData);
    }

    public Optional<AirportData> findAirportData(String iataCode) {
        return airportData.stream()
                .filter(data -> data.getIata().equals(iataCode))
                .findFirst();
    }

    public Map<AirportData, Integer> getRequestFrequency() {
        return Collections.unmodifiableMap(requestFrequency);
    }

    public Map<Double, Integer> getRadiusFreq() {
        return Collections.unmodifiableMap(radiusFreq);
    }

    public Optional<AirportData> deleteAirportData(String iata) {
        return findAirportData(iata)
                .map(data -> {
                    airportData.remove(data);
                    return data;
                });
    }
}