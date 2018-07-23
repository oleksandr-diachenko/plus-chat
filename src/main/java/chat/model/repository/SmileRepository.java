package chat.model.repository;

import chat.model.entity.Smile;

import java.util.Optional;

/**
 * @author Alexander Diachenko.
 */
public interface SmileRepository extends CRUDRepository<Smile>{

    Optional<Smile> getByName(final String name);
}
