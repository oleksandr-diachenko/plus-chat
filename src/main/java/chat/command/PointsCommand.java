package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;

import java.util.Optional;

public class PointsCommand implements ICommand {

    private UserRepository userRepository;
    private String nick;

    public PointsCommand(UserRepository userRepository, String nick) {
        this.userRepository = userRepository;
        this.nick = nick;
    }

    @Override
    public boolean canExecute(String command) {
        return "!points".equalsIgnoreCase(command);
    }

    @Override
    public String execute() {
        Optional<User> userByName = this.userRepository.getUserByName(this.nick);
        if (userByName.isPresent()) {
            User user = userByName.get();
            return user.getCustomName() + ", you have " + user.getPoints() + " points.";
        }
        return "";
    }
}
