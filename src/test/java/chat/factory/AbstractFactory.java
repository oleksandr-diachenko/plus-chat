package chat.factory;

import java.util.Optional;

/**
 * @author Oleksandr_Diachenko
 */
public abstract class AbstractFactory<T> {

    public abstract Optional<T> create();
}
