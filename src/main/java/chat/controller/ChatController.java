package chat.controller;

import chat.component.SettingsDialog;
import chat.util.StyleUtil;
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
    private CommandRepository commandRepository = new JSONCommandRepository();
    private int index = 0;


    @FXML
    public void initialize() {
        this.settings = AppProperty.getProperty("./settings/settings.properties");
        this.root.setStyle(StyleUtil.getRootStyle(
                this.settings.getProperty("root.base.color"),
                this.settings.getProperty("root.background.color")
        ));
        this.scrollPane.prefHeightProperty().bind(this.root.heightProperty());
        this.scrollPane.vvalueProperty().bind(this.container.heightProperty());
        startBot();
    }

    private void startBot() {
        final Thread thread = new Thread(() -> {
            final Properties connect = AppProperty.getProperty("./settings/twitch.properties");
            final Bot listener = new Bot(connect, this.userRepository, this.rankRepository, this.commandRepository);
            listener.addObserver(this);
            final Configuration config = new Configuration.Builder()
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
        return (Stage) this.container.getScene().getWindow();
    }

    private void openSettingsStage() {
        final SettingsDialog dialog = new SettingsDialog();
        dialog.openDialog(getStage(), this.root);
    }

    @Override
    public void update(final String nick, final String message) {
        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        final Optional<User> userByName = userRepository.getUserByName(nick);
        final Label image = new Label();
        if (userByName.isPresent()) {
            final User user = userByName.get();
            final Rank rank = rankRepository.getRankByExp(user.getExp());
            image.setId("rank-image");
            try (final FileInputStream fis = new FileInputStream(rank.getImagePath())) {
                final ImageView imageView = new ImageView(new Image(fis));
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                image.setGraphic(imageView);
            } catch (IOException exception) {
                logger.error(exception.getMessage(), exception);
                exception.printStackTrace();
            }
        }
        final TextFlow textFlow = new TextFlow();
        final Text name = new Text(StringUtil.getUTF8String(nick));
        name.setId("user-name");
        name.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), this.settings.getProperty("nick.font.color")));
        final Text separator = new Text(StringUtil.getUTF8String(": "));
        separator.setId("separator");
        separator.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), this.settings.getProperty("separator.font.color")));
        final Text mess = new Text(StringUtil.getUTF8String(message));
        mess.setId("user-message");
        mess.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), this.settings.getProperty("message.font.color")));
        textFlow.getChildren().addAll(name, separator, mess);
        hBox.getChildren().addAll(image, textFlow);
        this.messages.add(hBox);
        this.container.getChildren().add(this.messages.get(this.index));
        this.index++;
    }
}
