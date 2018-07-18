package chat.model.repository;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CRUDRepository<T> {

    Set<T> getAll();
}
