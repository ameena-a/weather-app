package com.weather.app.repository;

import com.weather.app.entity.WeatherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherStatusRepository extends JpaRepository<WeatherStatus, Long> {
    Optional<WeatherStatus> findByCityAndCountry(String city, String country);

}
