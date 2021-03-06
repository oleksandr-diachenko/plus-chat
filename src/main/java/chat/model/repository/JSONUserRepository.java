package chat.model.repository;

import chat.model.entity.User;
import chat.util.FileUtil;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
@Repository
@NoArgsConstructor
@Log4j2
public class JSONUserRepository implements UserRepository {

    private ObjectMapper mapper = new ObjectMapper();
    private Set<User> users;
    private String path;

    public JSONUserRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<User> getAll() {
        try {
            users =  mapper.readValue(FileUtil.readFile(path), new TypeReference<Set<User>>() {});
            return users;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
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
        flush();
        return user;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), users);
                } catch (IOException exception) {
                    log.error(exception.getMessage(), exception);
                    throw new RuntimeException("Users failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
