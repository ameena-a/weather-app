package com.weather.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentTest
public class GetWeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/v1/status?city=Melbourne&country=AU&openWeatherMapApiKey=dcb259bad64a4f2599fb448997ec8a29"))
                .andExpect(status().isOk());
     }

  
}