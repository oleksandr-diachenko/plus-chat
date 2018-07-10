package chat.observer;

/**
 * @author Alexander Diachenko.
 */
public interface Subject {

    void addObserver(final Observer observer);

    void removeObserver(final Observer observer);

    void notifyObserver(final String nick, final String message);
}
