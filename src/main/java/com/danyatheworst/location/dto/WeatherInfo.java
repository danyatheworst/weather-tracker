package com.danyatheworst.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherInfo {
    private Double temperature;
    private Double temperatureFeelsLike;
    private Double windSpeed;
    private String weatherState;
    private String description;
//    private LocalDateTime date;
}
