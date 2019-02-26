package chat.unit.repository;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

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
    public void getAllUsersTest() {
        final Set<User> users = this.userRepository.getAll();
        assertTrue(!users.isEmpty());
    }

    @Test
    public void getUserByNameTest() {
        final Optional<User> userByName = this.userRepository.getUserByName("p0sltlv");
        assertTrue(userByName.isPresent());
    }

    @Test
    public void getUserByIncorrectNameTest() {
        final Optional<User> userByName = this.userRepository.getUserByName("QWE");
        assertTrue(!userByName.isPresent());
    }
}
