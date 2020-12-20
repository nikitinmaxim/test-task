package com.test.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

/**
 * The Weather App REST endpoint allows clients to control app.
 *
 * @author code test administrator
 */
@RestController
@RequestMapping("/collect")
@RequiredArgsConstructor
public class RestWeatherSystemController {

    /**
     * Exit Weather App
     *
     * @return status code
     */
    @GetMapping("/exit")
    public Response exit() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}