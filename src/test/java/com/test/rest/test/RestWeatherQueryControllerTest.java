package com.test.rest.test;

import com.test.rest.dto.PointDataDto;
import com.test.rest.dto.PointDataTypeDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class RestWeatherQueryControllerTest extends BaseControllerTest {

    @Test
    void testPing() throws Exception {
        mvc.perform(get("/query/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datasize").value(0))
                .andExpect(jsonPath("$.iata_freq", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.radius_freq", Matchers.hasSize(1001)));

        update(new PointDataDto(1.0,2.0,3.0,4.0, 1, PointDataTypeDto.WIND));

        mvc.perform(get("/query/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datasize").value(1))
                .andExpect(jsonPath("$.iata_freq", Matchers.aMapWithSize(5)))
                .andExpect(jsonPath("$.radius_freq", Matchers.hasSize(1001)));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"BOS", "EWR", "JFK", "LGA", "MMU"})
    void testWeatherCode(String code) throws Exception {
        mvc.perform(get("/query/weather/" + code + "/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"CODE"})
    void testWeatherCodeNegative(String code) throws Exception {
        mvc.perform(get("/query/weather/" + code + "/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
    }

    @ParameterizedTest()
    @ValueSource(strings = { "0", "1"})
    void testWeatherRadius(String radius) throws Exception {
        mvc.perform(get("/query/weather/BOS/"+ radius))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void testWeatherRadiusNegative() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () -> {
            mvc.perform(get("/query/weather/BOS/qwe"))
                    .andExpect(status().isOk());
        });
    }
}
