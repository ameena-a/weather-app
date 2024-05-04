package com.weather.app.service;

import com.weather.app.dto.Geocordinates;
import com.weather.app.dto.WeatherRequest;
import com.weather.app.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
@Component
@Slf4j
public class WeatherService {

    private final GeoCoorinatorService geoCoorinatorService;
    private final WebClient webClient;

    @Value("${openWeatherMap.endpoint}")
    private String openWeatherMapEndPoint;

    @Value("${openWeatherMap.path}")
    private String openWeatherMapPath;

    public WeatherResponse getWeather(WeatherRequest weatherRequest) throws Exception {
        try {
            log.info("WeatherService:getWeather: Invoking Open Weather App API");
            Geocordinates geocordinates = geoCoorinatorService.invokeGeoCoordinatesService(weatherRequest);
            Mono<WeatherResponse> mono = webClient.get()
                    .uri(openWeatherMapEndPoint, uriBuilder -> buildUriPath(uriBuilder, geocordinates, weatherRequest))
                    .retrieve()
                    .bodyToMono(WeatherResponse.class);

            WeatherResponse response = mono.block();
            if (response == null) { throw new Exception("Response is null"); }
            log.info("WeatherService:getWeather: Successfully invoked Open Weather App API");
            return response;

        } catch (HttpStatusCodeException e) {
            throw new Exception(e.getMessage());
        }


    }


    private URI buildUriPath(UriBuilder uriBuilder, Geocordinates geocordinates, WeatherRequest weatherRequest) {
        return uriBuilder.path(openWeatherMapPath)
                .queryParam("lat", geocordinates.getLatitude())
                .queryParam("lon",  geocordinates.getLongitude())
                .queryParam("appid", weatherRequest.getOpenWeatherMapApiKey())
                .build();

    }
}
