package com.danyatheworst.openWeather.weatherApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException {
    private Integer cod;
    private String message;
}
