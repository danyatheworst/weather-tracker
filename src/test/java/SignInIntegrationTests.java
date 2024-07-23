import com.danyatheworst.AuthenticationService;
import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.CSession;
import com.danyatheworst.session.SessionRepository;
import com.danyatheworst.session.SessionService;
import com.danyatheworst.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes =
        {HibernateConfig.class,
                AuthenticationService.class,
                BCryptPasswordEncoder.class,
                UserService.class,
                UserRepository.class,
                SessionService.class,
                SessionRepository.class}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SignInIntegrationTests {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @Test
    public void itShouldSaveSessionAfterSignInFirst() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        SignInRequestDto signInRequestDto = new SignInRequestDto("user", "000000");

        //when
        UUID sessionId = this.authenticationService.authenticate(signInRequestDto);

        //then
        CSession session = this.sessionService.findBy(sessionId);
        assertNotNull(session);
        assertEquals(session.getUser().getLogin(), "user");
    }

    @Test
    public void itShouldUpdateSessionExpirationDate() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        UUID sessionId = this.authenticationService.authenticate(new SignInRequestDto("user", "000000"));
        CSession session = this.sessionService.findBy(sessionId);
        User user = session.getUser();
        LocalDateTime expirationTime = session.getExpiresAt();

        //when
        this.sessionService.updateExpirationTime(sessionId);
        CSession updatedSession = this.sessionService.findBy(sessionId);

        //then
        assertEquals(sessionId, updatedSession.getId());
        assertEquals(user.getId(), updatedSession.getUser().getId());
        assertEquals(user.getLogin(), updatedSession.getUser().getLogin());
        assertNotEquals(expirationTime, updatedSession.getExpiresAt());
    }

    @Test
    void itShouldThrowExceptionAfterSignInIfUserDoesNotExist() {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("userWhoDoesNotExist", "000000");

        //when and then
        assertThrows(NotFoundException.class, () -> {
            this.authenticationService.authenticate(signInRequestDto);
        });
    }

    @Test
    void itShouldThrowExceptionIfSessionIsExpired() {
        //given
        this.userService.create(new SignUpRequestDto("user", "000000", "000000"));
        //create session with 0 sec expirationTime
        UUID sessionId = this.authenticationService.authenticate(new SignInRequestDto("user", "000000"));
        CSession session = this.sessionService.findBy(sessionId);

        //when ant then
        assertThrows(NotFoundException.class, () -> this.sessionService.checkExpiration(session));
    }
}
