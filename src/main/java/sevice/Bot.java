package sevice;

import controller.ChatController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.entity.Command;
import model.entity.Rank;
import model.entity.User;
import model.repository.*;
import org.apache.log4j.Logger;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import util.AppProperty;
import util.StringUtil;
import util.TimeUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Bot extends ListenerAdapter {

    private final static Logger logger = Logger.getLogger(Bot.class);

    private Properties connect;
    private UserRepository userRepository = new JSONUserRepository();
    private CommandRepository commandRepository = new JSONCommandRepository();
    private RankRepository rankRepository = new JSONRankRepository();
    private Pane container;
    private List<HBox> messages;
    private Properties properties;
    private int index = 0;

    public Bot(Pane container, List<HBox> messages, Properties properties) {
        this.container = container;
        this.messages = messages;
        this.properties = properties;
        connect = AppProperty.getProperty("./settings/connect.properties");
    }

    @Override
    public void onConnect(ConnectEvent event) {
        updateUI("Connected!", Color.GREEN);
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        updateUI("Disconnected!", Color.RED);
    }

    private void updateUI(String message, Color color) {
        Platform.runLater(() -> {
            HBox hBox = new HBox();
            Label label = new Label(message);
            label.setTextFill(color);
            hBox.getChildren().add(label);
            messages.add(hBox);
            container.getChildren().add(messages.get(index));
            index++;
        });
    }

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        updateUser(nick);
        String message = event.getMessage();
        updateUI(nick, message);
        String command = getCommandFromMessage(message);
        if (command != null) {
            runCommand(event, command);
        }
    }

    private void updateUI(String nick, String message) {
        Platform.runLater(() -> {
            HBox hBox = new HBox();
            Optional<User> userByName = userRepository.getUserByName(nick);
            Label image = new Label();
            if (userByName.isPresent()) {
                User user = userByName.get();
                Rank rank = rankRepository.getRankByExp(user.getExp());
                image.setId("rank-image");
                try (FileInputStream fis = new FileInputStream(rank.getImagePath())) {
                    ImageView imageView = new ImageView(new Image(fis));
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    image.setGraphic(imageView);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    exception.printStackTrace();
                }
            }
            TextFlow textFlow = new TextFlow();
            Text name = new Text(StringUtil.getUTF8String(nick));
            name.setId("user-name");
            String nameStyle = properties.getProperty("nick.font.size") +
                    properties.getProperty("nick.font.color");
            name.setStyle(nameStyle);
            Text separator = new Text(StringUtil.getUTF8String(": "));
            separator.setId("separator");
            String separatorStyle = properties.getProperty("separator.font.size") +
                    properties.getProperty("separator.font.color");
            separator.setStyle(separatorStyle);
            Text mess = new Text(StringUtil.getUTF8String(message));
            mess.setId("user-message");
            String messageStyle = properties.getProperty("message.font.size") +
                    properties.getProperty("message.font.color");
            mess.setStyle(messageStyle);
            textFlow.getChildren().addAll(name, separator, mess);
            hBox.getChildren().addAll(image, textFlow);
            messages.add(hBox);
            container.getChildren().add(messages.get(index));
            index++;
        });
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
        updateUI(botName, message);
        ChatController.bot.sendIRC().message("#" + connect.getProperty("twitch.channel"), message);
    }

    private void updateUser(String nick) {
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            updateExistingUser(userByName.get());
        } else {
            createNewUser(nick);
        }
    }

    private void updateExistingUser(User userByName) {
        User user = new User();
        user.setName(userByName.getName());
        user.setFirstMessageDate(userByName.getFirstMessageDate());
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(userByName.getExp() + 1);
        userRepository.update(user);
    }

    private void createNewUser(String nick) {
        User user = new User();
        user.setName(nick);
        user.setFirstMessageDate(TimeUtil.getDateToString(new Date()));
        user.setLastMessageDate(TimeUtil.getDateToString(new Date()));
        user.setExp(1);
        userRepository.add(user);
    }
}
