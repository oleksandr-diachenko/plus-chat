package twitch;

import chat.model.entity.User;
import chat.model.repository.JSONUserRepository;
import chat.model.repository.UserRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
public class UserRepositoryTest {

    private UserRepository userRepository = new JSONUserRepository(getResource("/json/users.json"));

    @Test
    public void getAllUsersTest() {
        final Set<User> users = userRepository.getAll();
        assertTrue(!users.isEmpty());
    }

    @Test
    public void getUserByNameTest() {
        final Optional<User> userByName = userRepository.getByName("POSITIV");
        assertTrue(userByName.isPresent());
    }

    @Test
    public void getUserByIncorrectNameTest() {
        final Optional<User> userByName = userRepository.getByName("QWE");
        assertTrue(!userByName.isPresent());
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
