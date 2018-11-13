package chat.observer;

/**
 * @author Alexander Diachenko.
 */
public interface Observer {

    void update(String nick, String message);
}
