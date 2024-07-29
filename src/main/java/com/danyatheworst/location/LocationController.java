package com.danyatheworst.location;

import com.danyatheworst.location.dto.CreateLocationRequestDto;
import com.danyatheworst.location.dto.LocationInfo;
import com.danyatheworst.location.dto.WeatherInfo;
import com.danyatheworst.location.dto.WeatherLocationDto;
import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.openWeather.OpenWeatherApiService;
import com.danyatheworst.openWeather.weatherApiResponse.Weather;
import com.danyatheworst.openWeather.weatherApiResponse.WeatherApiResponse;
import com.danyatheworst.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LocationController {

    private final OpenWeatherApiService openWeatherApiService;
    private final LocationService locationService;

    public LocationController(OpenWeatherApiService openWeatherApiService, LocationService locationService) {
        this.openWeatherApiService = openWeatherApiService;
        this.locationService = locationService;
    }

    @GetMapping("search")
    public String search(@RequestParam(name = "q", defaultValue = "") String q, Model model)  {
        try {
            List<LocationApiDto> locations = this.openWeatherApiService.findLocationsBy(q);
            model.addAttribute("locationApiResponseDto", locations);
            return "search";

        } catch (Exception e) {
            return "search";
        }
    }

    @PostMapping("locations")
    public String create(
            CreateLocationRequestDto createLocationRequestDto,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) {
        try {
            //TODO: 500 and 409 (unique) exceptions handling
            this.locationService.save(createLocationRequestDto, (User) request.getAttribute("user"));
            return "index";
        } catch (Exception e) {
            return "search";
        }
    }

    @GetMapping("")
    public String fetchWeatherForAllLocationsBy(Model model, HttpServletRequest request) {
        long userId = ((User) request.getAttribute("user")).getId();
        List<Location> locations = this.locationService.findAllBy(userId);
        List<WeatherLocationDto> weatherLocationsDto = new ArrayList<>();

        for (Location location : locations) {
            WeatherApiResponse weather = this.openWeatherApiService.getWeatherBy(location.getLat(), location.getLon());
            LocationInfo locationInfo = getLocationIfo(location);
            WeatherInfo weatherInfo = getWeatherInfo(weather);
            WeatherLocationDto weatherLocationDto = new WeatherLocationDto(locationInfo, weatherInfo);
            weatherLocationsDto.add(weatherLocationDto);
            int a = 123;
        }
        model.addAttribute("weatherLocationsDto", weatherLocationsDto);
        return "index";
    }

    private static LocationInfo getLocationIfo(Location location) {
        Long id = location.getId();
        String name = location.getName();
        String country = location.getCountry();
        String state = location.getState();
        return new LocationInfo(id, name, country, state);
    }

    private static WeatherInfo getWeatherInfo(WeatherApiResponse weather) {
        Double temperature = weather.getMain().getTemperature();
        Double temperatureFeelsLike = weather.getMain().getTemperatureFeelsLike();
        Double windSpeed = weather.getWind().getSpeed();
        String weatherState = weather.getWeather().get(0).getCurrentState();
        String description = weather.getWeather().get(0).getDescription();
        return new WeatherInfo(temperature, temperatureFeelsLike, windSpeed, weatherState, description);
    }
}
