package thread;

/**
 * @author Alexander Diachenko.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String user, String message);
}
