package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class OrderCommand implements ICommand {

    private static final int MINIMAL_ORDER_POINTS = 600;
    private static final int ARGUMENTS_LENGTH = 3;
    private final UserRepository userRepository;
    private final String nick;
    private long points;
    private String game;

    public OrderCommand(final UserRepository userRepository, final String nick) {
        this.userRepository = userRepository;
        this.nick = nick;
    }

    @Override
    public boolean canExecute(String command) {
        String[] parts = command.split(" ");
        if (!correctArguments(parts)) {
            return false;
        }
        points = Long.parseLong(parts[1]);
        game = parts[2];
        return true;
    }

    private boolean correctArguments(String[] parts) {
        return parts.length >= ARGUMENTS_LENGTH
                && "!order".equalsIgnoreCase(parts[0])
                && StringUtils.isNumeric(parts[1]);
    }

    @Override
    public String execute() {
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            User user = userByName.get();
            if (lessThanMinimalOrder()) {
                return user.getCustomName() + ", minimal order is 1 hour (600 points)";
            }
            long userPoints = user.getPoints();
            if (notEnoughPoints(userPoints)) {
                return user.getCustomName() + ", you don't have enough points! (" + userPoints + ")";
            }
            user.setPoints(userPoints - points);
            userRepository.update(user);
            return user.getCustomName() + ", your order is accepted (" + game + ")";
        }
        return "";
    }

    private boolean lessThanMinimalOrder() {
        return points < MINIMAL_ORDER_POINTS;
    }

    private boolean notEnoughPoints(long userPoints) {
        return userPoints < points;
    }
}
