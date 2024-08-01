package com.danyatheworst.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherInfoDto {
    private String temperature;
    private String temperatureFeelsLike;
    private String windSpeed;
    private String description;
}
