package com.test.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
public class RestWeatherSystemController {

    @GetMapping("/exit")
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}