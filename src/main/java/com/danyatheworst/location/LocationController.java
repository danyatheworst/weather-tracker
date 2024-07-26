package com.danyatheworst.location;

import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.openWeather.OpenWeatherApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LocationController {

    private final OpenWeatherApiService openWeatherApiService;

    public LocationController(OpenWeatherApiService openWeatherApiService) {
        this.openWeatherApiService = openWeatherApiService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", defaultValue = "") String q, Model model)  {
        try {
            List<LocationApiDto> locations = this.openWeatherApiService.findLocationsBy(q);
            model.addAttribute("locationApiResponseDto", locations);
            return "search";

        } catch (Exception e) {
            return "search";
        }
    }
}
