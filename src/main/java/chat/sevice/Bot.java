package chat.sevice;

import chat.command.*;
import chat.controller.ChatController;
import chat.model.entity.User;
import chat.model.repository.CommandRepository;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;
import chat.observer.Observer;
import chat.observer.Subject;
import chat.util.TimeUtil;
import javafx.application.Platform;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Alexander Diachenko
 */
public class Bot extends ListenerAdapter implements Subject {

    private UserRepository userRepository;
    private RankRepository rankRepository;
    private CommandRepository commandRepository;
    private List<Observer> observers = new ArrayList<>();
    private Properties connect;
    private LocalDateTime start;

    public Bot(Properties connect, UserRepository userRepository,
               RankRepository rankRepository, CommandRepository commandRepository) {
        this.connect = connect;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.commandRepository = commandRepository;
    }

    @Override
    public void onConnect(ConnectEvent event) {
        start = LocalDateTime.now();
        String botName = connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, "Connected!");
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        String botName = connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, "Disconnected!");
    }

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        User user = updateUser(nick);
        String message = event.getMessage();
        notifyObserver(user.getName(), message);
        if (isCommand(message)) {
            runCommand(event.getUser().getNick(), message);
        }
    }

    private boolean isCommand(String message) {
        return message.startsWith("!");
    }

    private void runCommand(String nick, String command) {
        List<ICommand> commands = getCommands(nick);
        for (ICommand comm : commands) {
            if (comm.canExecute(command)) {
                sendMessage(comm.execute());
                break;
            }
        }
    }

    private List<ICommand> getCommands(String nick) {
        List<ICommand> commands = new ArrayList<>();
        commands.add(new RankCommand(nick, userRepository, rankRepository));
        commands.add(new UpCommand(start));
        commands.add(new RollCommand(userRepository, nick));
        commands.add(new PointsCommand(userRepository, nick));
        commands.add(new GameOrderCommand(userRepository, nick));
        commands.add(new JSONCommand(commandRepository));
        return commands;
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        ChatController.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(String message) {
        String botName = connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, message);
        ChatController.bot.sendIRC().message("#" + connect.getProperty("channel"), message);
    }

    private User updateUser(String nick) {
        return userRepository.getUserByName(nick)
                .map(this::updateExistingUser)
                .orElseGet(() -> createNewUser(nick));
    }

    private User updateExistingUser(User user) {
        user.setLastMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        long exp = user.getExp() + 1;
        user.setExp(exp);
        if (rankRepository.isNewRank(exp)) {
            user.setPoints(user.getPoints() + 500);
        }else {
            user.setPoints(user.getPoints() + 10);
        }
        return userRepository.update(user);
    }

    private User createNewUser(String nick) {
        User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        user.setLastMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        user.setExp(1);
        user.setPoints(10);
        return userRepository.add(user);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String nick, String message) {
        observers.forEach(observer -> Platform.runLater(() -> observer.update(nick, message)));
    }
}
