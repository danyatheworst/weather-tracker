package com.danyatheworst.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteLocationRequestDto {
    private BigDecimal lat;
    private BigDecimal lon;
}
