package com.danyatheworst.openWeather;


import com.danyatheworst.exceptions.InternalServerException;
import com.danyatheworst.exceptions.OpenWeatherApiException;
import com.danyatheworst.openWeather.weatherApiResponse.ApiException;
import com.danyatheworst.openWeather.weatherApiResponse.WeatherApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class OpenWeatherApiService {

    private static final String APP_ID = System.getenv("OPENWEATHER_API_KEY");
    private static final String BASE_API_URL = "https://api.openweathermap.org";
    private static final String GEOCODING_API_URL_SUFFIX = "/geo/1.0/direct?q=";
    private static final String WEATHER_API_URL_SUFFIX = "/data/2.5/weather?";

    private final HttpClient httpClient;
    private final JsonMapper jsonMapper = new JsonMapper();

    public OpenWeatherApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<LocationApiDto> findLocationsBy(String name) {
        try {
            URI uri = OpenWeatherApiService.buildUriForGeocodingRequest(name);
            return this.jsonMapper.readValue(this.getResponseBody(uri), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public WeatherApiResponse getWeatherBy(BigDecimal lat, BigDecimal lon) {
        try {
            URI uri = OpenWeatherApiService.buildUriForWeatherRequest(lat, lon);
            return this.jsonMapper.readValue(this.getResponseBody(uri), new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public static URI buildUriForGeocodingRequest(String locationName) {
        String encodedName = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
        String url = BASE_API_URL + GEOCODING_API_URL_SUFFIX + encodedName + "&limit=5" + "&appid=" + APP_ID;
        return URI.create(url);
    }

    public static URI buildUriForWeatherRequest(BigDecimal lat, BigDecimal lon) {
        return URI.create(
                BASE_API_URL + WEATHER_API_URL_SUFFIX + "lat=" + lat + "&lon=" + lon + "&units=metric" + "&appid=" + APP_ID
        );
    }

    private String getResponseBody(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri).build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            ApiException apiException = this.jsonMapper.readValue(response.body(), new TypeReference<>() {
            });
            throw new OpenWeatherApiException(apiException.getMessage(), apiException.getCod());
        }
        return response.body();
    }
}
