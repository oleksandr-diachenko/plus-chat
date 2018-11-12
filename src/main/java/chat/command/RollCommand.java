package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Random;

public class RollCommand implements ICommand {

    private UserRepository userRepository;
    private String nick;
    private long points;

    public RollCommand(final UserRepository userRepository, final String nick) {
        this.userRepository = userRepository;
        this.nick = nick;
    }

    @Override
    public boolean canExecute(final String command) {
        final String[] parts = command.split(" ");
        if (wrongArguments(parts)) {
            return false;
        }

        this.points = Long.parseLong(parts[1]);
        return true;
    }

    private boolean wrongArguments(final String[] parts) {
        return parts.length != 2
                && !"!roll".equalsIgnoreCase(parts[0])
                && !StringUtils.isNumeric(parts[1]);
    }

    @Override
    public String execute() {
        int percent = new Random().nextInt(100);
        final Optional<User> userByName = this.userRepository.getUserByName(this.nick);
        if (userByName.isPresent()) {
            final User user = userByName.get();
            long userPoints = user.getPoints();
            if (userPoints < this.points) {
                return user.getCustomName() + ", you don't have enough points! (" + userPoints + ")";
            }
            if (percent < 75) {
                user.setPoints(userPoints - this.points);
                this.userRepository.update(user);
                return user.getCustomName() + ", you lost =(";
            } else {
                user.setPoints(userPoints + this.points * 2);
                this.userRepository.update(user);
                return user.getCustomName() + ", you win =)";
            }
        }
        return "";
    }
}
