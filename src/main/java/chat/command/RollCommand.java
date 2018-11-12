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
            if (notEnoughPoints(userPoints)) {
                return user.getCustomName() + ", you don't have enough points! (" + userPoints + ")";
            }
            if (win(percent)) {
                updateUser(user, userPoints - this.points);
                return user.getCustomName() + ", you lost (" + userPoints + ")";
            } else {
                updateUser(user, userPoints + (this.points * 2));
                return user.getCustomName() + ", you win (" + userPoints + ")";
            }
        }
        return "";
    }

    private boolean notEnoughPoints(final long userPoints) {
        return userPoints < this.points;
    }

    private boolean win(final int percent) {
        return percent < 75;
    }

    private void updateUser(final User user, long points) {
        user.setPoints(points);
        this.userRepository.update(user);
    }
}
