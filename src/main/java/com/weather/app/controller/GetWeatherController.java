package com.weather.app.controller;

import com.weather.app.dto.ApiResponse;
import com.weather.app.dto.WeatherRequest;
import com.weather.app.dto.WeatherResponse;
import com.weather.app.entity.WeatherStatus;
import com.weather.app.repository.WeatherStatusRepository;
import com.weather.app.service.RateLimitService;
import com.weather.app.service.WeatherService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class GetWeatherController {

    private final WeatherService weatherService;
    private final RateLimitService rateLimitService;
    private final WeatherStatusRepository weatherStatusRepository;


    @GetMapping("/v1/status")
    public @ResponseBody ApiResponse getWeatherResponse(@RequestParam String city, @RequestParam String country, @RequestParam String openWeatherMapApiKey) throws Exception {
        log.info("GetWeatherController:getWeatherResponse: Getting Weather Response for the requested city : {}" , city);
        WeatherRequest weatherRequest = WeatherRequest.builder().city(city).country(country).openWeatherMapApiKey(openWeatherMapApiKey).build();  // we can use mapstruct as well to create mappers
        Bucket bucket = rateLimitService.resolveBucket(openWeatherMapApiKey); // limit accessing the api only 5 times in an hr , based on key
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);
            Optional<WeatherStatus> weatherStatus = weatherStatusRepository.findByCityAndCountry(city, country);
            if (weatherStatus.isEmpty()) {
                log.info("GetWeatherController:getWeatherResponse: Saving the data in DB for the city : {}" , city);
                weatherStatusRepository.save(WeatherStatus.builder().city(city).country(country).weatherDesc(weatherResponse.getWeather()[0].getDescription()).build());
                weatherStatus = weatherStatusRepository.findByCityAndCountry(city, country);
            }
            log.info("GetWeatherController:getWeatherResponse: Success");
            return ApiResponse.builder().status("SUCCESS").weatherDesc(weatherStatus.get().getWeatherDesc()).build();
        }
        log.info("GetWeatherController:getWeatherResponse: Failure");
        return ApiResponse.builder().status("FAILURE - OpenWeatherMapApiKey is allowed only 5 calls per one hour").weatherDesc("").build();
    }
}
