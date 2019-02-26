package chat.command;

import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;

import java.util.Optional;

/**
 * @author Alexander Diachenko
 */
public class RankCommand implements ICommand {

    private static final String COMMAND_NAME = "!rank";
    private String nick;
    private UserRepository userRepository;
    private RankRepository rankRepository;

    public RankCommand(String nick, UserRepository userRepository,
                       RankRepository rankRepository) {
        this.nick = nick;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
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
        String customName = user.getCustomName();
        Rank rank = rankRepository.getRankByExp(user.getExp());
        return customName + ", your rank " + rank.getName() + " (" + user.getExp() + " exp)";
    }
}
