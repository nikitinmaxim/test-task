package com.test.rest.test;

import com.test.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BaseControllerTest {
    private static final String APPLICATION_JSON_VALUE_UTF8 = MediaType.APPLICATION_JSON_VALUE
            + ";charset=UTF-8";


    @Autowired
    protected MockMvc mvc;


    protected MvcResult getResponse(String uri, int responseNum) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(APPLICATION_JSON_VALUE_UTF8)
                .accept(APPLICATION_JSON_VALUE_UTF8))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(responseNum, status);
        return mvcResult;
    }




}
