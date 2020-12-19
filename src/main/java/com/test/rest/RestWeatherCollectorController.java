package com.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.DataPoint;
import com.test.model.DataPointType;
import com.test.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
        return queryService.findAirport(iata)
                .map(airportData -> Response.ok(airportData).build())
                .orElseGet(() -> Response.status(404).build());
    }

    @PostMapping("/airport/{iata}/{latitude}/{longitude}")
    public Response createAirport(@PathVariable String iata, @PathVariable String latitude, @PathVariable String longitude) {
        int alatitude = Integer.parseInt(latitude);
        int alongitude = Integer.parseInt(longitude);
        return Response.ok(queryService.addAirport(iata, alatitude, alongitude)).build();
    }

    @DeleteMapping("/airport/{iata}")
    public Response deleteAirport(@PathVariable String iata) {
        return queryService.deleteAirport(iata)
                .map(airportData -> Response.ok().build())
                .orElseGet(() -> Response.status(404).build());
    }
}