package thread;

/**
 * @author Alexander Diachenko.
 */
public interface Subject {
    void registerObserver(final Observer observer);
    void removeObserver(final Observer observer);
    void notifyObservers();
}
