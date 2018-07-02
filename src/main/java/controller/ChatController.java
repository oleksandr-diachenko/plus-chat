package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import sevice.Bot;
import util.AppProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class ChatController {

    private final static Logger logger = Logger.getLogger(ChatController.class);

    public static PircBotX bot;
    @FXML
    private VBox container;
    @FXML
    private VBox root;
    @FXML
    private ScrollPane scrollPane;
    private List<HBox> messages = new ArrayList<>();


    @FXML
    public void initialize() {
        scrollPane.prefHeightProperty().bind(root.heightProperty());
        scrollPane.vvalueProperty().bind(container.heightProperty());
        startBot();
    }

    private void startBot() {
        Thread thread = new Thread(() -> {
            Properties connect = AppProperty.getProperty("./settings/connect.properties");
            Configuration config = new Configuration.Builder()
                    .setName(connect.getProperty("twitch.botname"))
                    .addServer("irc.chat.twitch.tv", 6667)
                    .setServerPassword(connect.getProperty("twitch.oauth"))
                    .addListener(new Bot(container, messages))
                    .addAutoJoinChannel("#" + connect.getProperty("twitch.channel"))
                    .buildConfiguration();
            bot = new PircBotX(config);
            try {
                bot.startBot();
            } catch (IOException | IrcException exception) {
                logger.error(exception.getMessage(), exception);
                exception.printStackTrace();
            }
        });
        thread.start();
    }
}
