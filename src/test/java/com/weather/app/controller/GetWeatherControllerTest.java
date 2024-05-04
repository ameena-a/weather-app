package com.weather.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@ActiveProfiles("local")
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetWeatherControllerTest {


    @Test
    void getWeather() {
        given().log().all().get("/weather-app/v1/status?city=Melbourne&country=AU&openWeatherMapApiKey=dcb259bad64a4f2599fb448997ec8a29").then().statusCode(200).assertThat()
                .body("status", equalTo("SUCCESS"));
    }
}