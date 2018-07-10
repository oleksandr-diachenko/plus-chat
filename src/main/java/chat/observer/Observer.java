package chat.observer;

/**
 * @author Alexander Diachenko.
 */
public interface Observer {

    void update(final String nick, final String message);
}
