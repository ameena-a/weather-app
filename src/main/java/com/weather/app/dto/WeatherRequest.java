package com.weather.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class WeatherRequest {
    private String city;
    private String country;
    private String openWeatherMapApiKey;
}
