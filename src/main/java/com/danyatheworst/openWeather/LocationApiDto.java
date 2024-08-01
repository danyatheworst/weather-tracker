package com.danyatheworst.openWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LocationApiDto {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
