package chat.command;

import chat.model.entity.Command;
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
            return true;
        }
        return false;
    }

    @Override
    public String execute() {
        return command.getResponse();
    }
}
