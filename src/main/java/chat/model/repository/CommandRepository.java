package chat.model.repository;

import chat.model.entity.Command;

import java.util.Optional;

/**
 * @author Alexander Diachenko.
 */
public interface CommandRepository extends CRUDRepository<Command> {

    Optional<Command> getCommandByName(final String name);
}
