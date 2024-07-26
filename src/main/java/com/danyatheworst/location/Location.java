package com.danyatheworst.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Long id;
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
}
