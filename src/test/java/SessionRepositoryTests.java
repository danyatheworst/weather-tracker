import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.session.CSession;
import com.danyatheworst.session.SessionRepository;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {HibernateConfig.class, SessionRepository.class, UserRepository.class})
public class SessionRepositoryTests {

    @Autowired
    private SessionRepository underTest;
    @Autowired
    private UserRepository userRepository;

    @Test
    void itShouldFindSessionById() {
        //given
        User user = new User("user", "000000");
        user.setId((long) 222);
        this.userRepository.save(user);
        CSession session = new CSession(user, LocalDateTime.now().plusHours(72));
        UUID sessionId = this.underTest.save(session);

        //when
        CSession sessionFound = this.underTest.findBy(sessionId);

        //then
        assertEquals(sessionId, sessionFound.getId());
        assertEquals(user.getId(), sessionFound.getUser().getId());
    }
}
