package com.weather.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(schema = "test", name = "weatherStatus")
@SuperBuilder(toBuilder = true)
@Data
@NoArgsConstructor
public class WeatherStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String city;

    private String country;

    private String weatherDesc;

}
