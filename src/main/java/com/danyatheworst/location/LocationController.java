package com.danyatheworst.location;

import com.danyatheworst.common.ErrorResponseDto;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.exceptions.OpenWeatherApiException;
import com.danyatheworst.location.dto.*;
import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.openWeather.OpenWeatherApiService;
import com.danyatheworst.openWeather.weatherApiResponse.WeatherApiResponse;
import com.danyatheworst.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("locations")
    public String search(@RequestParam(name = "q", defaultValue = "") String q,
                         Model model,
                         HttpServletResponse response) {
        try {
            List<LocationApiDto> locations = this.openWeatherApiService.findLocationsBy(q);
            model.addAttribute("locationApiResponseDto", locations);
        } catch (OpenWeatherApiException e) {
            if (e.getStatusCode() == HttpServletResponse.SC_BAD_REQUEST) {
                model.addAttribute("errorResponseDto", new ErrorResponseDto("Invalid location name"));
            } else {
                model.addAttribute("errorResponseDto", new ErrorResponseDto(e.getMessage()));
            }
            response.setStatus(e.getStatusCode());
        }
        return "locations";
    }

    @PostMapping("locations")
    public String create(
            CreateLocationRequestDto createLocationRequestDto,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            this.locationService.save(createLocationRequestDto, (User) request.getAttribute("user"));
            return "redirect:/";
        } catch (EntityAlreadyExistsException e) {
            model.addAttribute("errorResponseDto", new ErrorResponseDto(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "locations";
        }
    }

    @GetMapping("/")
    public String fetchWeatherOfUserLocations(Model model, HttpServletRequest request, HttpServletResponse response) {
        long userId = ((User) request.getAttribute("user")).getId();
        List<Location> locations = this.locationService.findAllBy(userId);
        List<WeatherLocationDto> weatherLocationsDto = new ArrayList<>();

        try {
            for (Location location : locations) {
                WeatherApiResponse weather = this.openWeatherApiService.getWeatherBy(location.getLat(), location.getLon());
                LocationInfo locationInfo = getLocationIfo(location);
                WeatherInfo weatherInfo = getWeatherInfo(weather);
                WeatherLocationDto weatherLocationDto = new WeatherLocationDto(locationInfo, weatherInfo);
                weatherLocationsDto.add(weatherLocationDto);
            }
            model.addAttribute("weatherLocationsDto", weatherLocationsDto);
        } catch (OpenWeatherApiException e) {
            response.setStatus(e.getStatusCode());
            model.addAttribute("errorResponseDto", new ErrorResponseDto(e.getMessage()));
        }
        return "index";
    }

    @PostMapping("/")
    public String removeUserLocation(DeleteLocationRequestDto deleteLocationRequestDto, HttpServletRequest request) {
            long userId = ((User) request.getAttribute("user")).getId();
            this.locationService.remove(deleteLocationRequestDto, userId);

            return "redirect:/";
    }

    private static LocationInfo getLocationIfo(Location location) {
        return new LocationInfo(
                location.getId(),
                location.getName(),
                location.getCountry(),
                location.getState(),
                location.getLat(),
                location.getLon()
        );
    }

    private static WeatherInfo getWeatherInfo(WeatherApiResponse weather) {
        Double temperature = weather.getMain().getTemperature();
        Double temperatureFeelsLike = weather.getMain().getTemperatureFeelsLike();
        Double windSpeed = weather.getWind().getSpeed();
        String weatherState = weather.getWeather().get(0).getCurrentState();
        String description = weather.getWeather().get(0).getDescription();
        LocalDateTime dateTime = weather.getDate();
        return new WeatherInfo(temperature, temperatureFeelsLike, windSpeed, weatherState, description, dateTime);
    }
}
