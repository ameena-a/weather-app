package com.weather.app.service;

import com.weather.app.dto.Geocordinates;
import com.weather.app.dto.WeatherRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoCoorinatorService {
    private final WebClient webClient;

    @Value("${geocoding.endpoint}")
    private String geocodingEndPoint;

    @Value("${geocoding.path}")
    private String geocodingPath;

    public Geocordinates invokeGeoCoordinatesService(WeatherRequest weatherRequest) throws Exception {
        try {
            log.info("GeoCoorinatorService:invokeGeoCoordinatesService: Invoking Geocoding API");
            Mono<Geocordinates[]> mono = webClient.get().
                    uri(geocodingEndPoint, uriBuilder -> buildUriPath(uriBuilder, weatherRequest))
                    .retrieve()
                    .bodyToMono(Geocordinates[].class);

            Geocordinates[] response = mono.block();
            if (response == null) { throw new Exception("Response is null"); }
            log.info("GeoCoorinatorService:invokeGeoCoordinatesService: Successfully invoked Geocoding API");
            return response[0];

        } catch (HttpStatusCodeException e) {
            throw new Exception(e.getMessage());
        }

    }

    private URI buildUriPath(UriBuilder uriBuilder, WeatherRequest weatherRequest) {
        return uriBuilder.path(geocodingPath)
                .queryParam("q", weatherRequest.getCity(),weatherRequest.getCountry())
                .queryParam("appid", weatherRequest.getOpenWeatherMapApiKey())
                .queryParam("limit", 1)
                .build();

    }
}



