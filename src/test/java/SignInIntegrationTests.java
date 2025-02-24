import com.danyatheworst.AuthenticationService;
import com.danyatheworst.config.TestConfig;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.Session;
import com.danyatheworst.session.SessionService;
import com.danyatheworst.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SignInIntegrationTests {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @Test
    public void itShouldSaveSessionAfterSignIn() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        SignInRequestDto signInRequestDto = new SignInRequestDto("user", "000000");

        //when
        UUID sessionId = this.authenticationService.authenticate(signInRequestDto);

        //then
        Session session = this.sessionService.findBy(sessionId);
        assertNotNull(session);
        assertEquals(session.getUser().getLogin(), "user");
    }

    @Test
    void itShouldThrowExceptionAfterSignInIfUserDoesNotExist() {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("userWhoDoesNotExist", "000000");

        //when and then
        assertThrows(NotFoundException.class, () -> this.authenticationService.authenticate(signInRequestDto));
    }

    @Test
    void itShouldThrowExceptionAfterSignInIfPasswordIsWrong() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        SignInRequestDto signInRequestDto = new SignInRequestDto("user", "000001");

        //when and then
        assertThrows(NotFoundException.class, () -> this.authenticationService.authenticate(signInRequestDto));
    }

    @Test
    void itShouldThrowExceptionIfSessionIsExpired() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        //create session with 0 sec expirationTime
        UUID sessionId = this.authenticationService.authenticate(new SignInRequestDto("user", "000000"));
        Session session = this.sessionService.findBy(sessionId);

        //when ant then
        assertThrows(NotFoundException.class, () -> this.sessionService.checkExpiration(session));
    }
}
