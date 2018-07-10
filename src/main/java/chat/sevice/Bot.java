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
import chat.util.AppProperty;
import chat.util.TimeUtil;

import java.util.*;

public class Bot extends ListenerAdapter implements Subject {

    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final CommandRepository commandRepository;
    private List<Observer> observers = new ArrayList<>();

    private Properties connect;

    public Bot(UserRepository userRepository, RankRepository rankRepository, CommandRepository commandRepository) {
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.commandRepository = commandRepository;
        connect = AppProperty.getProperty("./settings/connect.properties");
    }

    @Override
    public void onConnect(ConnectEvent event) {
        final String botName = connect.getProperty("twitch.botname");
        updateUser(botName);
        notifyObserver(botName, "Connected!");
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        final String botName = connect.getProperty("twitch.botname");
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
        String command = getCommandFromMessage(message);
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
    private String getCommandFromMessage(String message) {
        String[] msgParts = message.split(" ");
        if (msgParts[0].startsWith("!")) {
            return msgParts[0];
        } else {
            return null;
        }
    }

    private void runCommand(GenericMessageEvent event, String command) {
        if (command.equalsIgnoreCase("!rank")) {
            runRankCommand(event);
        } else {
            runOtherCommands(command);
        }
    }

    private void runOtherCommands(String command) {
        Optional<Command> commandByName = commandRepository.getCommandByName(command);
        if (commandByName.isPresent()) {
            Command comm = commandByName.get();
            sendMessage(comm.getResponse());
        }
    }

    private void runRankCommand(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            User user = userByName.get();
            Rank rank = rankRepository.getRankByExp(user.getExp());
            sendMessage(nick + ", your rank " + rank.getName() + " (" + user.getExp() + " exp)");
        }
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        ChatController.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(String message) {
        String botName = connect.getProperty("twitch.botname");
        updateUser(botName);
        notifyObserver(botName, message);
        ChatController.bot.sendIRC().message("#" + connect.getProperty("twitch.channel"), message);
    }

    private User updateUser(String nick) {
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            return updateExistingUser(userByName.get());
        } else {
            return createNewUser(nick);
        }
    }

    private User updateExistingUser(User userByName) {
        User user = new User();
        user.setName(userByName.getName());
        user.setFirstMessageDate(userByName.getFirstMessageDate());
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(userByName.getExp() + 1);
        userRepository.update(user);
        return user;
    }

    private User createNewUser(String nick) {
        User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.getDateToString(new Date()));
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(1);
        userRepository.add(user);
        return user;
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
        for (Observer observer : observers) {
            Platform.runLater(() -> {
                observer.update(nick, message);
            });
        }
    }
}
