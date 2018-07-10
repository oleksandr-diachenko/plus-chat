package chat.controller;

import chat.component.SettingsDialog;
import chat.component.StyleUtil;
import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.*;
import chat.observer.Observer;
import chat.util.StringUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
public class ChatController implements Observer {

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
    private RankRepository rankRepository = new JSONRankRepository();
    private UserRepository userRepository = new JSONUserRepository();
    private CommandRepository  commandRepository = new JSONCommandRepository();
    private int index = 0;


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
            Properties connect = AppProperty.getProperty("./settings/twitch.properties");
            Bot listener = new Bot(connect, userRepository, rankRepository, commandRepository);
            listener.addObserver(this);
            Configuration config = new Configuration.Builder()
                    .setName(connect.getProperty("botname"))
                    .addServer("irc.chat.twitch.tv", 6667)
                    .setServerPassword(connect.getProperty("oauth"))
                    .addListener(listener)
                    .addAutoJoinChannel("#" + connect.getProperty("channel"))
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

    @Override
    public void update(String nick, String message) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            Optional<User> userByName = userRepository.getUserByName(nick);
            Label image = new Label();
            if (userByName.isPresent()) {
                final User user = userByName.get();
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
            name.setStyle(StyleUtil.getTextStyle(settings.getProperty("font.size"), settings.getProperty("nick.font.color")));
            Text separator = new Text(StringUtil.getUTF8String(": "));
            separator.setId("separator");
            separator.setStyle(StyleUtil.getTextStyle(settings.getProperty("font.size"), settings.getProperty("separator.font.color")));
            Text mess = new Text(StringUtil.getUTF8String(message));
            mess.setId("user-message");
            mess.setStyle(StyleUtil.getTextStyle(settings.getProperty("font.size"), settings.getProperty("message.font.color")));
            textFlow.getChildren().addAll(name, separator, mess);
            hBox.getChildren().addAll(image, textFlow);
            messages.add(hBox);
            container.getChildren().add(messages.get(index));
            index++;
    }
}
