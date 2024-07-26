package it;

import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.exceptions.EntityAlreadyExistsException;
import com.danyatheworst.user.SignUpRequestDto;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import com.danyatheworst.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes =
        {HibernateConfig.class,
        UserService.class,
        UserRepository.class,
        BCryptPasswordEncoder.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SignUpIntegrationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void itShouldSaveUserAfterSignUp() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("user", "000000", "000000");

        //when
        this.userService.create(signUpRequestDto);

        //then
        Optional<User> savedUser = this.userRepository.findBy(signUpRequestDto.getLogin());
        assertTrue(savedUser.isPresent());
        assertEquals(signUpRequestDto.getLogin(), savedUser.get().getLogin());
    }

    @Test
    public void itShouldThrowExceptionWhenLoginAlreadyExists() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("user", "000000", "000000");
        this.userService.create(signUpRequestDto);

        //when and then
        assertThrows(EntityAlreadyExistsException.class, () -> {
            this.userService.create(signUpRequestDto);
        });
    }
}
