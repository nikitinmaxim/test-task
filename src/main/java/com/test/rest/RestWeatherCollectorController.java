package com.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.DataPoint;
import com.test.model.DataPointType;
import com.test.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.test.exception.WeatherException;
import com.test.service.CollectorService;

import javax.ws.rs.core.Response;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */
@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
public class RestWeatherCollectorController {

    private final CollectorService collectorService;
    private final QueryService queryService;
    private final ObjectMapper objectMapper;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Response ping() {
        return Response.ok("ready").build();
    }

    @PostMapping("/weather/{iata}/{pointType}")
    public Response updateWeather(@PathVariable(name = "iata") String iataCode, @PathVariable String pointType, @RequestBody String datapointJson) {
        try {
            collectorService.addDataPoint(iataCode, DataPointType.valueOf(pointType.toUpperCase()), objectMapper.readValue(datapointJson, DataPoint.class));
            return Response.ok().build();
        } catch (WeatherException e) {
            return Response.status(422).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }


    @GetMapping("/airports")
    public Response getAirports() {
        return Response.ok(collectorService.getAirports()).build();
    }

    @GetMapping("/airport/{iata}")
    public Response getAirport(@PathVariable String iata) {
        return queryService.findAirportData(iata)
                .map(airportData -> Response.ok(airportData).build())
                .orElseGet(() -> Response.status(404).build());
    }

    @GetMapping("/exit")
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}