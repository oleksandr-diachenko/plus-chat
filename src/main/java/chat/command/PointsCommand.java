package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;

import java.util.Optional;

public class PointsCommand implements ICommand {

    private static final String COMMAND_NAME = "!points";
    private UserRepository userRepository;
    private String nick;

    public PointsCommand(UserRepository userRepository, String nick) {
        this.userRepository = userRepository;
        this.nick = nick;
    }

    @Override
    public boolean canExecute(String command) {
        return COMMAND_NAME.equalsIgnoreCase(command);
    }

    @Override
    public String execute() {
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isEmpty()) {
            return "";
        }
        User user = userByName.get();
        return user.getCustomName() + ", you have " + user.getPoints() + " points.";
    }
}
