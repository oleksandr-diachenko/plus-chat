package chat.controller;

import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import chat.sevice.Bot;
import chat.util.AppProperty;
import chat.util.ResourceBundleControl;

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
        root.setStyle(getRootStyle());
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
        return (Stage)container.getScene().getWindow();
    }

    private void openSettingsStage() {
        Stage stage = new Stage();
        stage.setResizable(false);
        String language = settings.getProperty("root.language");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        Region root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/settings.fxml"), bundle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UndecoratorScene undecorator = new UndecoratorScene(stage, root);
        root.setStyle(getRootStyle());
        stage.setScene(undecorator);
        stage.setTitle("Settings");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getStage().getScene().getWindow());
        stage.show();
    }

    private String getRootStyle() {
        return "-fx-base: " + settings.getProperty("root.base.color") + ";" +
                "-fx-background: " + settings.getProperty("root.background.color") + ";" +
                "-fx-font-family: \"" + settings.getProperty("root.font.family") + "\";";
    }
}
