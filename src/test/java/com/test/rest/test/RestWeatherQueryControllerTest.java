package com.test.rest.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RestWeatherQueryControllerTest extends BaseControllerTest {

    @Test
    void testPing() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/query/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.datasize").value(5));
    }


    @ParameterizedTest()
    @ValueSource(strings = {"BOS", "EWR", "JFK", "LGA", "MMU"})
    void testWeatherCode(String code) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/query/weather/" + code + "/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"CODE"})
    void testWeatherCodeNegative(String code) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/query/weather/" + code + "/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404));
    }

    @ParameterizedTest()
    @ValueSource(strings = { "0", "1"})
    void testWeatherRadius(String radius) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/query/weather/BOS/"+ radius))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
    }

    @Test
    void testWeatherRadiusNegative() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () -> {
            mvc.perform(MockMvcRequestBuilders.get("/query/weather/BOS/qwe"))
                    .andExpect(status().isOk());
        });
    }


}
