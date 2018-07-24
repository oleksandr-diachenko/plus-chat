package chat.model.repository;

import chat.model.entity.User;

import java.util.Optional;


/**
 * @author Alexander Diachenko.
 */
public interface UserRepository extends CRUDRepository<User> {

    Optional<User> getUserByName(final String name);
}
