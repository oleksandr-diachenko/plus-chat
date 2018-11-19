package chat.command;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Random;

public class RollCommand implements ICommand {

    private static final int ARGUMENTS_LENGTH = 2;
    private static final int WIN_PERCENT = 75;
    private UserRepository userRepository;
    private String nick;
    private long points;

    public RollCommand(UserRepository userRepository, String nick) {
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
        return true;
    }

    private boolean correctArguments(String[] parts) {
        return parts.length >= ARGUMENTS_LENGTH
                && "!roll".equalsIgnoreCase(parts[0])
                && StringUtils.isNumeric(parts[1]);
    }

    @Override
    public String execute() {
        int percent = new Random().nextInt(100);
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            User user = userByName.get();
            long userPoints = user.getPoints();
            if (notEnoughPoints(userPoints)) {
                return user.getCustomName() + ", you don't have enough points!" + getStringPoints(userPoints);
            }
            if (win(percent)) {
                userPoints += getWinPoints();
                updateUser(user, userPoints);
                return user.getCustomName() + ", you won" + " " + getWinPoints() + " points!" + getStringPoints(userPoints);
            } else {
                userPoints -= points;
                updateUser(user, userPoints);
                return user.getCustomName() + ", you lost" + " " + points + " points!" + getStringPoints(userPoints);
            }
        }
        return "";
    }

    private long getWinPoints() {
        double random = new Random().nextDouble() + 1;
        return (long) (points * random);
    }

    private String getStringPoints(long userPoints) {
        return " (You have " + userPoints + " points)";
    }

    private boolean notEnoughPoints(long userPoints) {
        return userPoints < points;
    }

    private boolean win(int percent) {
        return percent > WIN_PERCENT;
    }

    private void updateUser(User user, long points) {
        user.setPoints(points);
        userRepository.update(user);
    }
}
