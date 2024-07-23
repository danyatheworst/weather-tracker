import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.exceptions.NotFoundException;
import com.danyatheworst.session.CSession;
import com.danyatheworst.session.SessionRepository;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {HibernateConfig.class, SessionRepository.class, UserRepository.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SessionRepositoryTests {

    @Autowired
    private SessionRepository underTest;
    @Autowired
    private UserRepository userRepository;

    private User user;


    private void setUp() {
        this.user = new User("user", "000000");
        this.userRepository.save(this.user);
    }

    @Test
    void itShouldFindSessionById() {
        //given
        this.setUp();
        CSession session = new CSession(this.user, LocalDateTime.now().plusHours(72));
        UUID sessionId = this.underTest.save(session);

        //when
        Optional<CSession> sessionFound = this.underTest.findBy(sessionId);

        //then
        assertTrue(sessionFound.isPresent());
        assertEquals(sessionId, sessionFound.get().getId());
        assertEquals(this.user.getId(), sessionFound.get().getUser().getId());
    }

    @Test
    public void itShouldUpdateSessionExpirationDate() {
        //given
        this.setUp();
        CSession session = new CSession(this.user, LocalDateTime.now().plusHours(72));
        this.underTest.save(session);
        UUID sessionId = session.getId();
        LocalDateTime expirationTime = session.getExpiresAt();

        //when
        this.underTest.update(sessionId, LocalDateTime.now().plusHours(72));
        Optional<CSession> updatedSession = this.underTest.findBy(sessionId);


        //then
        assertTrue(updatedSession.isPresent());
        assertEquals(sessionId, updatedSession.get().getId());
        assertEquals(this.user.getId(), updatedSession.get().getUser().getId());
        assertEquals(this.user.getLogin(), updatedSession.get().getUser().getLogin());
        assertNotEquals(expirationTime, updatedSession.get().getExpiresAt());
    }

    @Test
    void itShouldThrowExceptionWhenUpdatingNonExistentSession() {
        //given
        UUID nonExistentSessionId = UUID.randomUUID();
        //then and when
        assertThrows(NotFoundException.class, () -> this.underTest.update(nonExistentSessionId, LocalDateTime.now()));
    }
}
