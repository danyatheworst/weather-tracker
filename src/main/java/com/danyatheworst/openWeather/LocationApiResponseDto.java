package com.danyatheworst.openWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationApiResponseDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}
