package chat.model.repository;

import chat.model.entity.Direct;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface DirectRepository {

    Set<Direct> getDirects();

    Optional<Direct> getDirectByWord(final String word);
}
