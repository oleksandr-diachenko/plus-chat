package chat.repository;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnAllUserCommands() {
        Set<User> users = userRepository.getAll();
        assertFalse(users.isEmpty());
    }

    @Test
    public void shouldReturnUserWhenUserNameCorrect() {
        Optional<User> userByName = userRepository.getUserByName("p0sltlv");
        assertTrue(userByName.isPresent());
    }

    @Test
    public void shouldNotReturnUserWhenUserNameIncorrect() {
        Optional<User> userByName = userRepository.getUserByName("QWE");
        assertFalse(userByName.isPresent());
    }
}
