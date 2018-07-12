package chat.sevice;

import chat.controller.ChatController;
import chat.observer.Observer;
import chat.observer.Subject;
import javafx.application.Platform;
import chat.model.entity.Command;
import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import chat.util.TimeUtil;

import java.util.*;

public class Bot extends ListenerAdapter implements Subject {

    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final CommandRepository commandRepository;
    private List<Observer> observers = new ArrayList<>();
    private Properties connect;

    public Bot(final Properties connect, final UserRepository userRepository, final RankRepository rankRepository, final CommandRepository commandRepository) {
        this.connect = connect;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.commandRepository = commandRepository;
    }

    @Override
    public void onConnect(final ConnectEvent event) {
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
            runCommand(event, command);
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

    private void runCommand(final GenericMessageEvent event, final String command) {
        if (command.equalsIgnoreCase("!rank")) {
            runRankCommand(event);
        } else {
            runOtherCommands(command);
        }
    }

    private void runOtherCommands(final String command) {
        final Optional<Command> commandByName = this.commandRepository.getCommandByName(command);
        if (commandByName.isPresent()) {
            final Command comm = commandByName.get();
            sendMessage(comm.getResponse());
        }
    }

    private void runRankCommand(final GenericMessageEvent event) {
        final String nick = event.getUser().getNick();
        final Optional<User> userByName = this.userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            final User user = userByName.get();
            final Rank rank = this.rankRepository.getRankByExp(user.getExp());
            sendMessage(nick + ", your rank " + rank.getName() + " (" + user.getExp() + " exp)");
        }
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
        final Optional<User> userByName = this.userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            return updateExistingUser(userByName.get());
        } else {
            return createNewUser(nick);
        }
    }

    private User updateExistingUser(final User user) {
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(user.getExp() + 1);
        this.userRepository.update(user);
        return user;
    }

    private User createNewUser(final String nick) {
        final User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.getDateToString(new Date()));
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(1);
        this.userRepository.add(user);
        return user;
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
        for (Observer observer : this.observers) {
            Platform.runLater(() -> observer.update(nick, message));
        }
    }
}