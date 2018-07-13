package twitch;

import chat.model.entity.User;
import chat.model.repository.JSONUserRepository;
import chat.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
public class UserRepositoryTest {

    private UserRepository userRepository = new JSONUserRepository(getResource("/json/users.json"));

    @Test
    public void getAllUsers() {
        final Set<User> users = userRepository.getUsers();
        assertTrue(!users.isEmpty());
    }

    @Test
    public void getUserByName() {
        final Optional<User> userByName = userRepository.getUserByName("POSITIV");
        assertTrue(userByName.isPresent());
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
