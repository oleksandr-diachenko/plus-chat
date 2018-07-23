package chat.model.repository;

import chat.model.entity.Direct;

import java.util.Optional;

/**
 * @author Alexander Diachenko.
 */
public interface DirectRepository extends CRUDRepository<Direct>{

    Optional<Direct> getByWord(final String word);
}
