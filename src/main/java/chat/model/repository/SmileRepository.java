package chat.model.repository;

import chat.model.entity.Smile;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface SmileRepository {

    Set<Smile> getSmiles();

    Optional<Smile> getSmileByName(final String name);
}
