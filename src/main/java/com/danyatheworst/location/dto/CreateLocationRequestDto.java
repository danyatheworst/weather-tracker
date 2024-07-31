package com.danyatheworst.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocationRequestDto {
    @NotNull
    private String name;

    @NotNull
    private double lat;

    @NotNull
    private double lon;

    @NotNull
    private String country;

    private String state;
}
