package com.danyatheworst.location;

import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.openWeather.OpenWeatherApiService;
import com.danyatheworst.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("search")
public class LocationController {

    private final OpenWeatherApiService openWeatherApiService;
    private final LocationService locationService;

    public LocationController(OpenWeatherApiService openWeatherApiService, LocationService locationService) {
        this.openWeatherApiService = openWeatherApiService;
        this.locationService = locationService;
    }

    @GetMapping("")
    public String search(@RequestParam(name = "q", defaultValue = "") String q, Model model)  {
        try {
            List<LocationApiDto> locations = this.openWeatherApiService.findLocationsBy(q);
            model.addAttribute("locationApiResponseDto", locations);
            return "search";

        } catch (Exception e) {
            return "search";
        }
    }

    @PostMapping("")
    public String add(
            AddingLocationRequestDto addingLocationRequestDto,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) {
        try {
            //TODO: 500 and 409 (unique) exceptions handling
            this.locationService.save(addingLocationRequestDto, (User) request.getAttribute("user"));
            return "index";
        } catch (Exception e) {
            return "search";
        }
    }
}
