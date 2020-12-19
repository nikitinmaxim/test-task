package com.test.rest.test;

import com.test.model.DataPoint;
import com.test.model.DataPointType;
import org.hamcrest.Matchers;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.datasize").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iata_freq", Matchers.aMapWithSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.radius_freq", Matchers.hasSize(1001)));

        update(new DataPoint(1.0,2.0,3.0,4.0, 1, DataPointType.WIND));

        mvc.perform(MockMvcRequestBuilders.get("/query/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.datasize").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iata_freq", Matchers.aMapWithSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.radius_freq", Matchers.hasSize(1001)));
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
