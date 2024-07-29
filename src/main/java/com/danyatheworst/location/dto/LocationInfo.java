package com.danyatheworst.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationInfo {
    private Long id;
    private String name;
    private String country;
    private String state;
}
