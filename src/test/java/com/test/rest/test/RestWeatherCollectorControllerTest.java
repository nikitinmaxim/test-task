package com.test.rest.test;

import com.test.data.DataContainer;
import com.test.rest.dto.PointDataDto;
import com.test.rest.dto.PointDataTypeDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RestWeatherCollectorControllerTest extends BaseControllerTest {

    @Autowired
    private DataContainer dataContainer;

    @BeforeEach
    void cleanUp() {
        dataContainer.cleanUp();
    }

    @Test
    void testPing() throws Exception {
        mvc.perform(get("/collect/ping"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.entity").value("ready"));
    }

    @Test
    void testGetAirports() throws Exception {
        mvc.perform(get("/collect/airports"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.entity", Matchers.containsInAnyOrder("EWR","MMU","BOS","LGA","JFK")));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"EWR","MMU","BOS","LGA","JFK"})
    void testGetAirport(String code) throws Exception {
        mvc.perform(get("/collect/airport/" + code))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.entity").isNotEmpty());
    }

    @Test
    void testAddAirport() throws Exception {
        mvc.perform(post("/collect/airport/RND/47/39"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.entity").isNotEmpty());

        mvc.perform(get("/collect/airport/RND"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.entity").isNotEmpty());
    }

    @Test
    void testDeleteAirport() throws Exception {
        mvc.perform(delete("/collect/airport/BOS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        mvc.perform(get("/collect/airport/BOS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testUpdateWeather() throws Exception {
        update(new PointDataDto(1.0,2.0,3.0,4.0, 1, PointDataTypeDto.WIND))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }
}
