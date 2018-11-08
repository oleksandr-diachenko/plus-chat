package chat.sevice;

import chat.command.ICommand;
import chat.command.JSONCommand;
import chat.command.RankCommand;
import chat.command.UpCommand;
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

    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final CommandRepository commandRepository;
    private List<Observer> observers = new ArrayList<>();
    private Properties connect;
    private LocalDateTime start;

    public Bot(final Properties connect, final UserRepository userRepository,
               final RankRepository rankRepository, final CommandRepository commandRepository) {
        this.connect = connect;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.commandRepository = commandRepository;
    }

    @Override
    public void onConnect(final ConnectEvent event) {
        this.start = LocalDateTime.now();
        final String botName = this.connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, "Connected!");
    }

    @Override
    public void onDisconnect(final DisconnectEvent event) {
        final String botName = this.connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, "Disconnected!");
    }

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(final GenericMessageEvent event) {
        final String nick = event.getUser().getNick();
        final User user = updateUser(nick);
        final String message = event.getMessage();
        notifyObserver(user.getName(), message);
        final String command = getCommandFromMessage(message);
        if (command != null) {
            runCommand(event.getUser().getNick(), command);
        }
    }

    /**
     * The command will always be the first part of the message
     * We can split the string into parts by spaces to get each word
     * The first word if it starts with our command notifier "!" will get returned
     * Otherwise it will return null
     */
    private String getCommandFromMessage(final String message) {
        final String[] msgParts = message.split(" ");
        if (msgParts[0].startsWith("!")) {
            return msgParts[0];
        } else {
            return null;
        }
    }

    private void runCommand(final String nick, final String command) {
        final List<ICommand> commands = getCommands(nick);
        for (ICommand comm : commands) {
            if (comm.canExecute(command)) {
                sendMessage(comm.execute());
                break;
            }
        }
    }

    private List<ICommand> getCommands(final String nick) {
        final List<ICommand> commands = new ArrayList<>();
        commands.add(new RankCommand(nick, this.userRepository, this.rankRepository));
        commands.add(new UpCommand(this.start));
        commands.add(new JSONCommand(this.commandRepository));
        return commands;
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(final PingEvent event) {
        ChatController.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(final String message) {
        final String botName = this.connect.getProperty("botname");
        updateUser(botName);
        notifyObserver(botName, message);
        ChatController.bot.sendIRC().message("#" + this.connect.getProperty("channel"), message);
    }

    private User updateUser(final String nick) {
        return this.userRepository.getUserByName(nick)
                .map(this::updateExistingUser)
                .orElseGet(() -> createNewUser(nick));
    }

    private User updateExistingUser(final User user) {
        user.setLastMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        user.setExp(user.getExp() + 1);
        return this.userRepository.update(user);
    }

    private User createNewUser(final String nick) {
        final User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        user.setLastMessageDate(TimeUtil.getDateToString(LocalDateTime.now()));
        user.setExp(1);
        return this.userRepository.add(user);
    }

    @Override
    public void addObserver(final Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(final Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserver(final String nick, final String message) {
        this.observers.forEach(observer -> Platform.runLater(() -> observer.update(nick, message)));
    }
}
