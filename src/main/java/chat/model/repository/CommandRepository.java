package chat.model.repository;

import chat.model.entity.Command;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CommandRepository {

    Set<Command> getCommands();

    Optional<Command> getCommandByName(String name);
}
