package model.repository;

import model.entity.User;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface UserRepository {

    Set<User> getUsers();

    Optional<User> getUserByName(String name);

    void add(User user);

    void update(User user);
}
