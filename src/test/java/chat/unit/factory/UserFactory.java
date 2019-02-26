package chat.unit.factory;

import chat.model.entity.User;

import java.util.Optional;

/**
 * @author Oleksandr_Diachenko
 */
public class UserFactory extends AbstractFactory<User> {

    @Override
    public Optional<User> create() {
        User user = new User();
        user.setName("p0sltlv");
        user.setCustomName("POSITIV");
        user.setFirstMessageDate("1970-01-01 12:00");
        user.setLastMessageDate("1970-01-01 12:01");
        user.setExp(10);
        user.setPoints(1000);
        return Optional.of(user);
    }
}
