package chat.factory;

import chat.model.entity.Command;
import chat.model.entity.Status;

import java.util.Optional;

/**
 * @author Oleksandr_Diachenko
 */
public class JsonCommandFactory extends AbstractFactory<Command> {

    @Override
    public Optional<Command> create() {
        Command command = new Command();
        command.setName("!fire");
        command.setDescription("Test command");
        command.setResponse("Fire in the hall!");
        command.setStatus(Status.enabled);
        return Optional.of(command);
    }
}
