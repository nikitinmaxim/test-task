package com.test.rest.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.Application;
import com.test.rest.dto.PointDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions update(PointDataDto data) throws Exception {
        return mvc.perform(post("/collect/weather/BOS/" + data.getType().name())
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
