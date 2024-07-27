package it;

import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.location.AddingLocationRequestDto;
import com.danyatheworst.location.Location;
import com.danyatheworst.location.LocationRepository;
import com.danyatheworst.location.LocationService;
import com.danyatheworst.openWeather.LocationApiDto;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes =
        {HibernateConfig.class, 
        LocationService.class,
        LocationRepository.class,
        UserRepository.class
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocationIntegrationTests {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void itShouldThrowEntityAlreadyExistExceptionWhenSaveNotUniqueTrackedLocation() {
        //given
        this.userRepository.save(new User("user1", "000000"));
        AddingLocationRequestDto addingLocationRequestDto = new AddingLocationRequestDto(
                "Rome",
                new BigDecimal("41.8933203"),
                new BigDecimal("12.4829321")
        );
        User user = this.userRepository.findBy("user1").get();

        //when and then
        this.locationService.save(addingLocationRequestDto, user);
        assertThrows(EntityAlreadyExistsException.class, () -> this.locationService.save(addingLocationRequestDto, user));
    }
}
