package chat.controller;

import chat.component.SettingsDialog;
import chat.component.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import chat.sevice.Bot;
import chat.util.AppProperty;

import java.io.*;
import java.util.*;

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
    private Properties settings;


    @FXML
    public void initialize() {
        settings = AppProperty.getProperty("./settings/settings.properties");
        root.setStyle(StyleUtil.getRootStyle(settings.getProperty("root.base.color"), settings.getProperty("root.background.color")));
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
                    .addListener(new Bot(container, messages, settings))
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
        thread.setDaemon(true);
        thread.start();
    }

    public void settingsOnAction() {
        openSettingsStage();
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }

    private void openSettingsStage() {
        SettingsDialog dialog = new SettingsDialog();
        dialog.openDialog(getStage(), this.root);
    }
}
