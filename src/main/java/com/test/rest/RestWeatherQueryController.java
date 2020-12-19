package com.test.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.AtmosphericInformation;
import com.test.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class RestWeatherQueryController {

    private final QueryService queryService;
    private final ObjectMapper objectMapper;

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return queryService.queryPingResponse();
        /*Map<String, Object> pingResponse = queryService.queryPingResponse();
        try {
            return objectMapper.writeValueAsString(pingResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
         */
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radius the radius in km
     *
     * @return a list of atmospheric information
     */
    @GetMapping("/weather/{iata}/{radius}")
    public Response weather(@PathVariable String iata, @PathVariable String radius) {
        double aradius = radius == null || radius.trim().isEmpty() ? 0 : Double.parseDouble(radius);

        return queryService.findAirportData(iata)
                .map(data -> Response.ok(queryService.queryWeather(iata, aradius)).build())
                .orElseGet(() -> Response.status(404).build());
    }
}