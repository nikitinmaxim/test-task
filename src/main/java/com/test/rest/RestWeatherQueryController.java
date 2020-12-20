package com.test.rest;

import com.test.rest.mappers.AirportDataMapper;
import com.test.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * The Weather App REST endpoint allows clients to query health stats.
 *
 * @author code test administrator
 */
@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class RestWeatherQueryController {

    private final QueryService queryService;
    private final AirportDataMapper airportDataMapper;

    /**
     * Retrieve request frequency information.
     *
     * @return frequency information as a string
     */
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return queryService.queryPingResponse();
    }

    /**
     * Given a query param extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the 3 letter IATA code
     * @param radius the radius in km
     *
     * @return a list of atmospheric information
     */
    @GetMapping("/weather/{iata}/{radius}")
    public Response weather(@PathVariable String iata, @PathVariable String radius) {
        double aradius = radius == null || radius.trim().isEmpty() ? 0 : Double.parseDouble(radius);

        return queryService.findAirport(iata)
                .map(airportData -> queryService.queryWeather(iata, aradius))
                .map(info -> airportDataMapper.toDto(info))
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}