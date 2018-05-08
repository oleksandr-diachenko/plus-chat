package model.repository;

import model.entity.Command;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface CommandRepository {

    Set<Command> getCommands();
}
