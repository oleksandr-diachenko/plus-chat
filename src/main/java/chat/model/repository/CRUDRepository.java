package chat.model.repository;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CRUDRepository<T> {

    Set<T> getAll();

    T add(final T t);

    T update(final T t);

    T delete(final T t);
}
