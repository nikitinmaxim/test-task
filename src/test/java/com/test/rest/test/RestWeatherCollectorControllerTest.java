package com.test.rest.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.DataPoint;
import com.test.model.DataPointType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RestWeatherCollectorControllerTest extends BaseControllerTest {

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void testPing() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/collect/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entity").value("ready"));
    }

    @Test
    void testGetAirports() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/collect/airports"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entity", Matchers.containsInAnyOrder("EWR","MMU","BOS","LGA","JFK")));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"EWR","MMU","BOS","LGA","JFK"})
    void testGetAirport(String code) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/collect/airport/" + code))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entity").isNotEmpty());
    }



    @Test
    void testUpdateWeather() throws Exception {
        update(new DataPoint(1.0,2.0,3.0,4.0, 1, DataPointType.WIND))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
    }

    private ResultActions update(DataPoint data) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post("/collect/weather/BOS/" + data.getType().name() )
                .content(objectMapper.writeValueAsString(data))
                .contentType(MediaType.APPLICATION_JSON)
        );

    }
}
