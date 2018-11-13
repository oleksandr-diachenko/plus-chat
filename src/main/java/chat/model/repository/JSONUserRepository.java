package chat.model.repository;

import chat.model.entity.User;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
@Repository
public class JSONUserRepository implements UserRepository {

    private final static Logger logger = LogManager.getLogger(JSONUserRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<User> users;
    private String path;

    public JSONUserRepository() {
        //do nothing
    }

    public JSONUserRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<User> getAll() {
        try {
            users = new HashSet<>(
                    mapper.readValue(JSONParser.readFile(path), new TypeReference<List<User>>() {
                    }));
            return users;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new HashSet<>();
    }

    @Override
    public Optional<User> getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public User add(User user) {
        users.add(user);
        flush();
        return user;
    }

    @Override
    public User update(User user) {
        users.remove(user);
        users.add(user);
        flush();
        return user;
    }

    @Override
    public User delete(User user) {
        users.remove(user);
        return user;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), users);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Users failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
