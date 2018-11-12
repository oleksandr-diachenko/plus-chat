package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;

import java.util.Optional;

public class PointsCommand implements ICommand {

    private final UserRepository userRepository;
    private final String nick;

    public PointsCommand(final UserRepository userRepository, final String nick) {
        this.userRepository = userRepository;
        this.nick = nick;
    }

    @Override
    public boolean canExecute(final String command) {
        return "!points".equalsIgnoreCase(command);
    }

    @Override
    public String execute() {
        Optional<User> userByName = this.userRepository.getUserByName(this.nick);
        if(userByName.isPresent()) {
            final User user = userByName.get();
            return user.getCustomName() + ", you have " + user.getPoints() + " points.";
        }
        return "";
    }
}
