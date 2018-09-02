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


    private String nick;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public RankCommand(final String nick, final UserRepository userRepository, final RankRepository rankRepository) {
        this.nick = nick;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public boolean canExecute(final String command) {
        return "!rank".equalsIgnoreCase(command);
    }

    @Override
    public String execute() {
        final Optional<User> userByName = this.userRepository.getUserByName(this.nick);
        if (!userByName.isPresent()) {
            return "";
        }
        final User user = userByName.get();
        String customName = this.nick;
        if (user.hasCustomName()) {
            customName = user.getCustomName();
        }
        final Rank rank = this.rankRepository.getRankByExp(user.getExp());
        return customName + ", your rank " + rank.getName() + " (" + user.getExp() + " exp)";
    }
}
