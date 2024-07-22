import com.danyatheworst.config.HibernateConfig;
import com.danyatheworst.user.User;
import com.danyatheworst.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringJUnitConfig(classes = {HibernateConfig.class, UserRepository.class})
public class UserRepositoryTests {

    @Autowired
    private UserRepository underTest;

    @Test
    public void itShouldFindUserByLogin() {
        //given
        String login = "user";
        User user = new User(login, "user123");
        this.underTest.save(user);

        //when
        Optional<User> userFounded = this.underTest.findBy(login);

        //then
        assertTrue(userFounded.isPresent());
        assertEquals(userFounded.get().getLogin(), login);
    }
}
