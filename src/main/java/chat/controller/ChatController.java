package chat.controller;

import chat.component.SettingsDialog;
import chat.model.entity.Direct;
import chat.model.entity.Rank;
import chat.model.entity.Smile;
import chat.model.entity.User;
import chat.model.repository.*;
import chat.observer.Observer;
import chat.sevice.Bot;
import chat.util.AppProperty;
import chat.util.StringUtil;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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
    private RankRepository rankRepository;
    private UserRepository userRepository;
    private CommandRepository commandRepository;
    private SmileRepository smileRepository;
    private DirectRepository directRepository;
    private int index = 0;

    @FXML
    public void initialize() {
        this.rankRepository = new JSONRankRepository("./data/ranks.json");
        this.userRepository = new JSONUserRepository("./data/users.json");
        this.smileRepository = new JSONSmileRepository("./data/smiles.json");
        this.commandRepository = new JSONCommandRepository("./data/commands.json");
        this.directRepository = new JSONDirectRepository("./data/directs.json");
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
        hBox.setAlignment(Pos.TOP_LEFT);
        final Optional<User> userByName = userRepository.getUserByName(nick);
        final TextFlow textFlow = new TextFlow();
        if (userByName.isPresent()) {
            final User user = userByName.get();
            final Label image = getRankImage(user);
            textFlow.getChildren().add(image);
        }
        final Text name = getText(nick, "user-name", this.settings.getProperty("nick.font.color"));
        final Text separator = getText(": ", "separator", this.settings.getProperty("separator.font.color"));
        textFlow.getChildren().addAll(name, separator);
        final List<Node> nodes = getMessage(message, this.settings.getProperty("message.font.color"), this.settings.getProperty("direct.message.font.color"));
        nodes.iterator().forEachRemaining(node -> textFlow.getChildren().add(node));
        hBox.getChildren().add(textFlow);
        this.messages.add(hBox);
        this.container.getChildren().add(this.messages.get(this.index));
        this.index++;
    }

    private List<Node> getMessage(final String message, final String color, final String directColor) {
        final List<Node> nodes = new ArrayList<>();
        final String utf8Message = StringUtil.getUTF8String(message);
        final String[] words = utf8Message.split(" ");
        for (String word : words) {
            final Optional<Smile> smileByName = this.smileRepository.getSmileByName(word);
            if (smileByName.isPresent()) {
                final Label image = new Label();
                final Smile smile = smileByName.get();
                final ImageView imageView = getSmile(smile);
                image.setGraphic(imageView);
                nodes.add(image);
            } else {
                final Text text = new Text(word + " ");
                if (isDirect(words)) {
                    text.setId("user-direct-message");
                    text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), directColor));
                } else {
                    text.setId("user-message");
                    text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), color));
                }
                nodes.add(text);
            }
        }
        return nodes;
    }

    private boolean isDirect(final String[] words) {
        boolean isDirect = false;
        for (String word : words) {
            final Optional<Direct> directByWord = directRepository.getDirectByWord(word);
            if (directByWord.isPresent()) {
                isDirect = true;
                break;
            }
        }
        return isDirect;
    }

    private ImageView getSmile(Smile smile) {
        try (final FileInputStream fis = new FileInputStream(smile.getImagePath())) {
            return new ImageView(new Image(fis));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return null;
    }

    private Label getRankImage(final User user) {
        final Label image = new Label();
        final Rank rank = rankRepository.getRankByExp(user.getExp());
        image.setId("rank-image");
        try (final FileInputStream fis = new FileInputStream(rank.getImagePath())) {
            final ImageView imageView = new ImageView(new Image(fis));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            image.setGraphic(imageView);
            return image;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new Label();
    }

    private Text getText(final String string, final String id, final String color) {
        final Text text = new Text(StringUtil.getUTF8String(string));
        text.setId(id);
        text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty("font.size"), color));
        return text;
    }

    public void setSettings(final Properties settings) {
        this.settings = settings;
    }
}
