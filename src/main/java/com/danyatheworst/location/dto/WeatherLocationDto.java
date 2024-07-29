package com.danyatheworst.location.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherLocationDto {
    private LocationInfo locationInfo;
    private WeatherInfo weatherInfo;
}
