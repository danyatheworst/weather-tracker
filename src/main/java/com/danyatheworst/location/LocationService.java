package com.danyatheworst.location;

import com.danyatheworst.location.dto.CreateLocationRequestDto;
import com.danyatheworst.location.dto.DeleteLocationRequestDto;
import com.danyatheworst.user.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllBy(long userId) {
        return this.locationRepository.findAllBy(userId);
    }

    public long save(CreateLocationRequestDto createLocationRequestDto, User user) {
        Location location = new Location(
                createLocationRequestDto.getName(),
                createLocationRequestDto.getLat(),
                createLocationRequestDto.getLon(),
                user,
                createLocationRequestDto.getCountry(),
                createLocationRequestDto.getState()
        );
        return this.locationRepository.save(location);
    }

    public void remove(DeleteLocationRequestDto deleteLocationRequestDto, long userId) {
        this.locationRepository.removeBy(deleteLocationRequestDto.getLat(), deleteLocationRequestDto.getLon(), userId);
    }
}
