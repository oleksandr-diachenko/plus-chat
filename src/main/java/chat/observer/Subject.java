package chat.observer;

/**
 * @author Alexander Diachenko.
 */
public interface Subject {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(String nick, String message);
}
