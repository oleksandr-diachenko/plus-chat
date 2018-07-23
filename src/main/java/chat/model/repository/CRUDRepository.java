package chat.model.repository;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CRUDRepository<T> {

    Set<T> getAll();

    T add(T t);

    T update(T t);

    T delete(T t);
}
