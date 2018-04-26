package model.repository;

import model.entity.Command;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CommandRepository {

    Set<Command> getCommands();

    Command getCommandByName(String name);

    void add(Command command);

    void update(Command command);

    void delete(Command command);
}
