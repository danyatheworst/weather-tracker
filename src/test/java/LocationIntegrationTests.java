import com.danyatheworst.config.TestConfig;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.location.Location;
import com.danyatheworst.location.dto.CreateLocationRequestDto;
import com.danyatheworst.location.LocationService;
import com.danyatheworst.location.dto.DeleteLocationRequestDto;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocationIntegrationTests {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void itShouldThrowEntityAlreadyExistExceptionWhenSaveNotUniqueTrackedLocation() {
        //given
        this.userRepository.save(new User("user", "000000"));
        CreateLocationRequestDto createLocationRequestDto = new CreateLocationRequestDto(
                "Rome",
                41.8933203,
                12.4829321,
                "IT",
                "Lazio"
        );
        User user = this.userRepository.findBy("user").get();

        //when and then
        this.locationService.save(createLocationRequestDto, user);
        assertThrows(EntityAlreadyExistsException.class, () -> this.locationService.save(createLocationRequestDto, user));
    }

    @Test
    void shouldNotRemoveAnotherUserLocationWhenUserDeletesIt() {
        //given
        this.userRepository.save(new User("first_user", "000000"));
        this.userRepository.save(new User("second_user", "000000"));

        double lat = 41.8933203;
        double lon = 12.4829321;
        CreateLocationRequestDto createLocationRequestDto = new CreateLocationRequestDto(
                "Rome",
                lat,
                lon,
                "IT",
                "Lazio"
        );
        User user = this.userRepository.findBy("first_user").get();
        User user1 = this.userRepository.findBy("second_user").get();

        this.locationService.save(createLocationRequestDto, user);
        this.locationService.save(createLocationRequestDto, user1);

        //when
        this.locationService.remove(
                new DeleteLocationRequestDto(lat, lon),
                user.getId()
        );

        //then
        List<Location> secondUserTrackedLocations = this.locationService.findAllBy(user1.getId());
        assertEquals(1, secondUserTrackedLocations.size());
        assertEquals(secondUserTrackedLocations.get(0).getLat(), lat);
        assertEquals(secondUserTrackedLocations.get(0).getLon(), lon);
    }
}
