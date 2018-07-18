package chat.model.repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CRUDRepository<T> {

    Set<T> getAll();

    Optional<T> getByName(final String name);

    T add(T t);

    T update(T t);

    T delete(T t);
}
