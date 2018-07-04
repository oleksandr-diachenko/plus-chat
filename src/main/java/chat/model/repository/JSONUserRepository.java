package chat.model.repository;

import chat.model.entity.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import chat.util.JSONParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class JSONUserRepository implements UserRepository {

    private final static Logger logger = Logger.getLogger(JSONUserRepository.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Set<User> users;

    public JSONUserRepository() {
        users = getUsers();
    }

    @Override
    public Set<User> getUsers() {
        try {
            return new HashSet<>(mapper.readValue(JSONParser.readFile("./data/users.json"), new TypeReference<List<User>>() {
            }));
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
    public void add(User user) {
        users.add(user);
        flush();
    }

    @Override
    public void update(User user) {
        users.remove(user);
        users.add(user);
        flush();
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream("./data/users.json"), users);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                }
            }
        });
        thread.start();
    }
}
