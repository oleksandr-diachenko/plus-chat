package chat.sevice;

import chat.bot.AbstractBotStarter;
import chat.command.*;
import chat.model.entity.Status;
import chat.model.entity.User;
import chat.model.repository.CommandRepository;
import chat.model.repository.OrderRepository;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;
import chat.util.AppProperty;
import chat.util.TimeUtil;
import javafx.application.Platform;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @author Alexander Diachenko
 */
@Service
public class Bot extends ListenerAdapter implements ApplicationEventPublisherAware {

    private static final String BOTNAME = "botname";
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private CommandRepository commandRepository;
    private Properties connectProperty;
    private Properties commandsProperty;
    private LocalDateTime start;
    private OrderRepository orderRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public Bot(UserRepository userRepository, RankRepository rankRepository,
               CommandRepository commandRepository,
               OrderRepository orderRepository,
               @Qualifier("twitchProperties") AppProperty twitchProperties,
               @Qualifier("commandsProperties") AppProperty commandsProperties) {
        this.orderRepository = orderRepository;
        this.connectProperty = twitchProperties.loadProperty();
        this.commandsProperty = commandsProperties.loadProperty();
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.commandRepository = commandRepository;
    }

    @Override
    public void onConnect(ConnectEvent event) {
        start = LocalDateTime.now();
        String botName = connectProperty.getProperty(BOTNAME);
        updateUser(botName);
        notifyObservers(botName, "Connected!");
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        String botName = connectProperty.getProperty(BOTNAME);
        updateUser(botName);
        notifyObservers(botName, "Disconnected!");
    }

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        User user = updateUser(nick);
        String message = event.getMessage();
        notifyObservers(user.getName(), message);
        if (isCommand(message)) {
            runCommand(event.getUser().getNick(), message);
        }
    }

    private boolean isCommand(String message) {
        return message.startsWith("!");
    }

    private void runCommand(String nick, String command) {
        for (ICommand comm : getCommands(nick)) {
            if (comm.canExecute(command)) {
                sendMessage(comm.execute());
                break;
            }
        }
    }

    private List<ICommand> getCommands(String nick) {
        List<ICommand> commands = new LinkedList<>();
        commands.add(new RankCommand(nick, userRepository, rankRepository));
        commands.add(new UpCommand(start));
        commands.add(new RollCommand(userRepository, nick, new Random()));
        commands.add(new PointsCommand(userRepository, nick));
        commands.add(new OrderCommand(userRepository, nick, orderRepository));
        commands.add(new JSONCommand(commandRepository));
        return getEnabledCommands(commands);
    }

    private List<ICommand> getEnabledCommands(List<ICommand> commands) {
        List<ICommand> result = new LinkedList<>();
        for (ICommand command : commands) {
            String className = command.getClass().getSimpleName();
            String commandName = StringUtils.remove(className, "Command").toLowerCase();
            if (isEnabled(commandName)) {
                result.add(command);
            }
        }
        return result;
    }

    private boolean isEnabled(String commandName) {
        try {
            Status status = Status.valueOf(commandsProperty.getProperty(commandName));
            return status == Status.enabled;
        } catch (Exception exception) {
            return true;
        }
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        AbstractBotStarter.bot.sendRaw().rawLineNow(String.format("PONG %s %s", event.getPingValue(), System.lineSeparator()));
    }

    private void sendMessage(String message) {
        String botName = connectProperty.getProperty(BOTNAME);
        updateUser(botName);
        notifyObservers(botName, message);
        AbstractBotStarter.bot.sendIRC().message("#" + connectProperty.getProperty("channel"), message);
    }

    private User updateUser(String nick) {
        return userRepository.getUserByName(nick)
                .map(this::updateExistingUser)
                .orElseGet(() -> createNewUser(nick));
    }

    private User updateExistingUser(User user) {
        user.setLastMessageDate(TimeUtil.formatDate(LocalDateTime.now()));
        long exp = user.getExp() + 1;
        user.setExp(exp);
        if (rankRepository.isNewRank(exp)) {
            user.setPoints(user.getPoints() + 500);
        } else {
            user.setPoints(user.getPoints() + 10);
        }
        return userRepository.update(user);
    }

    private User createNewUser(String nick) {
        User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.formatDate(LocalDateTime.now()));
        user.setLastMessageDate(TimeUtil.formatDate(LocalDateTime.now()));
        user.setExp(1);
        user.setPoints(10);
        return userRepository.add(user);
    }

    private void notifyObservers(String nick, String message) {
        Platform.runLater(() -> applicationEventPublisher.publishEvent(new MessageEvent(this, nick, message)));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
