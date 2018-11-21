package chat.command;

import chat.model.entity.Command;
import chat.model.entity.Status;
import chat.model.repository.CommandRepository;

import java.util.Optional;

/**
 * @author Alexander Diachenko
 */
public class JSONCommand implements ICommand {

    private CommandRepository commandRepository;
    private Command command;

    public JSONCommand(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    @Override
    public boolean canExecute(String command) {
        Optional<Command> commandByName = commandRepository.getCommandByName(command);
        if (commandByName.isPresent()) {
            this.command = commandByName.get();
            Status status = this.command.getStatus();
            return status == Status.enabled;
        }
        return false;
    }

    @Override
    public String execute() {
        return command.getResponse();
    }
}
