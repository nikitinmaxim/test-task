package com.test.rest;

import com.test.rest.dto.PointDataDto;
import com.test.rest.mappers.AirportDataMapper;
import com.test.rest.mappers.PointDataMapper;
import com.test.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.test.exception.WeatherException;
import com.test.service.CollectorService;

import javax.ws.rs.core.Response;

/**
 * The Weather App REST endpoint allows clients to collect airport information.
 *
 * @author code test administrator
 */
@Slf4j
@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
public class RestWeatherCollectorController {

    private final CollectorService collectorService;
    private final QueryService queryService;
    private final PointDataMapper pointDataMapper;
    private final AirportDataMapper airportDataMapper;

    /**
     *  Do nothing
     *
     * @return not empty string
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Response ping() {
        return Response.ok("ready").build();
    }

    /**
     *  Add weather information
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType an weather information type
     * @param dto an weather information
     * @return
     */
    @PostMapping("/weather/{iata}/{pointType}")
    public Response updateWeather(@PathVariable(name = "iata") String iataCode, @PathVariable String pointType, @RequestBody PointDataDto dto) {
        try {
            collectorService.addDataPoint(iataCode, pointDataMapper.toEntity(pointType), pointDataMapper.toEntity(dto));
            return Response.ok().build();
        } catch (WeatherException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }

    /**
     *  List known airport IATA code
     *
     * @return list of strings
     */
    @GetMapping("/airports")
    public Response getAirports() {
        return Response.ok(collectorService.getAirports()).build();
    }

    /**
     *  Get airport information
     *
     * @param iataCode the 3 letter IATA code
     * @return airport data
     */
    @GetMapping("/airport/{iata}")
    public Response getAirport(@PathVariable(name = "iata") String iataCode) {
        return queryService.findAirport(iataCode)
                .map(airportData -> airportDataMapper.toDto(airportData))
                .map(dto -> Response.ok(dto).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     *  Add new airport if not exist
     *
     * @param iataCode the 3 letter IATA code
     * @param latitude coordinate
     * @param longitude coordinate
     * @return created airport data
     */
    @PostMapping("/airport/{iata}/{latitude}/{longitude}")
    public Response createAirport(@PathVariable(name = "iata") String iataCode, @PathVariable String latitude, @PathVariable String longitude) {
        try {
            int alatitude = Integer.parseInt(latitude);
            int alongitude = Integer.parseInt(longitude);
            return Response.ok(queryService.addAirport(iataCode, alatitude, alongitude)).build();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Delete airport
     *
     * @param iataCode the 3 letter IATA code
     * @return status code
     */
    @DeleteMapping("/airport/{iata}")
    public Response deleteAirport(@PathVariable(name = "iata") String iataCode) {
        return queryService.deleteAirport(iataCode)
                .map(airportData -> Response.ok().build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}