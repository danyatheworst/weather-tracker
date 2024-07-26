package com.danyatheworst.exceptions;

import lombok.Getter;

public class OpenWeatherApiException extends RuntimeException{
    @Getter
    private final int statusCode;

    public OpenWeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
