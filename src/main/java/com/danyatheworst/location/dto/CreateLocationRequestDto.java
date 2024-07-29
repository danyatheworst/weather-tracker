package com.danyatheworst.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocationRequestDto {
    @NotNull
    private String name;

    @NotNull
    private BigDecimal lat;

    @NotNull
    private BigDecimal lon;

    @NotNull
    private String country;

    private String state;
}
