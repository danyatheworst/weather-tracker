package com.danyatheworst.location;

import com.danyatheworst.user.User;
import org.springframework.stereotype.Service;


@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Long save(AddingLocationRequestDto addingLocationRequestDto, User user) {
        Location location = new Location(
                addingLocationRequestDto.getName(),
                addingLocationRequestDto.getLat(),
                addingLocationRequestDto.getLon(),
                user
        );
        return this.locationRepository.save(location);
    }
}
