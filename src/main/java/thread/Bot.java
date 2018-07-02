package thread;

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
import sevice.ChatService;
import util.AppProperty;
import util.StringUtil;
import util.TimeUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Bot extends ListenerAdapter {

    private final static Logger logger = Logger.getLogger(Bot.class);

    private Properties connect;
    private UserRepository userRepository = new UserRepositoryImpl();
    private CommandRepository commandRepository = new CommandRepositoryImpl();
    private RankRepository rankRepository = new RankRepositoryImpl();
    private Pane container;
    private List<HBox> messages;
    private int index = 0;

    public Bot(Pane container, List<HBox> messages) {
        this.container = container;
        this.messages = messages;
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
            User user = userRepository.getUserByName(nick);
            Rank rank = rankRepository.getRankByExp(user.getExp());
            Label image = new Label();
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
            TextFlow textFlow = new TextFlow();
            Text name = new Text(StringUtil.getUTF8String(nick));
            name.setId("user-name");
            Text separator = new Text(StringUtil.getUTF8String(": "));
            separator.setId("separator");
            Text mess = new Text(StringUtil.getUTF8String(message));
            mess.setId("user-message");
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
        Command commandByName = commandRepository.getCommandByName(command);
        sendMessage(commandByName.getResponse());
    }

    private void runRankCommand(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        User userByName = userRepository.getUserByName(nick);
        Rank rank = rankRepository.getRankByExp(userByName.getExp());
        sendMessage(nick + ", your rank " + rank.getName() + " (" + userByName.getExp() + " exp)");
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        ChatService.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(String message) {
        ChatService.bot.sendIRC().message("#" + connect.getProperty("twitch.channel"), message);
    }

    private void updateUser(String nick) {
        User userByName = userRepository.getUserByName(nick);
        if (userByName == null) {
            createNewUser(nick);
        } else {
            updateExistingUser(userByName);
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
