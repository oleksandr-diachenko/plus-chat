package chat.observer;

import chat.model.entity.User;

/**
 * @author Alexander Diachenko.
 */
public interface Observer {

    void update(User user, String message);
}
