package chat.observer;

import chat.model.entity.User;

/**
 * @author Alexander Diachenko.
 */
public interface Subject {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(User user, String message);
}
