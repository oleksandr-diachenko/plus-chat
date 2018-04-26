package model;

import java.io.IOException;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface UserRepository {

    Set<User> getUsers();

    User getUserByName(String name);

    void add(User user) throws IOException;

    void update(User user) throws IOException;
}
