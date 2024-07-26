package com.danyatheworst.location;

import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Long save(LocationApiDto locationApiDto, User user) {
        Location location = new Location(locationApiDto.getName(), locationApiDto.getLat(), locationApiDto.getLon(), user);
        return this.locationRepository.save(location);
    }
}
