package chat.controller;

import chat.PlusChatFX;
import chat.component.SettingsDialog;
import chat.model.entity.Direct;
import chat.model.entity.Rank;
import chat.model.entity.Smile;
import chat.model.entity.User;
import chat.model.repository.*;
import chat.observer.Observer;
import chat.sevice.Bot;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StringUtil;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class ChatController implements Observer {

    private final static Logger logger = LogManager.getLogger(ChatController.class);
    private static final String IMG_PIN_ENABLED_PNG_PATH = "/img/pin-enabled.png";
    private static final String IMG_PIN_DISABLED_PNG_PATH = "/img/pin-disabled.png";

    public static PircBotX bot;
    @FXML
    private Button onTop;
    @FXML
    private Button setting;
    @FXML
    private VBox container;
    @FXML
    private VBox root;
    @FXML
    private ScrollPane scrollPane;
    private List<TextFlow> messages = new ArrayList<>();
    private Properties settings;
    private RankRepository rankRepository;
    private UserRepository userRepository;
    private CommandRepository commandRepository;
    private SmileRepository smileRepository;
    private DirectRepository directRepository;
    private int messageIndex = 0;
    private boolean isOnTop;

    @FXML
    public void initialize() {
        this.rankRepository = new JSONRankRepository("./data/ranks.json");
        this.userRepository = new JSONUserRepository("./data/users.json");
        this.smileRepository = new JSONSmileRepository("./data/smiles.json");
        this.commandRepository = new JSONCommandRepository("./data/commands.json");
        this.directRepository = new JSONDirectRepository("./data/directs.json");
        this.settings = AppProperty.getProperty("./settings/settings.properties");
        this.isOnTop = Boolean.parseBoolean(this.settings.getProperty(Settings.ROOT_ALWAYS_ON_TOP));
        onTopInit();
        this.root.setStyle(StyleUtil.getRootStyle(
                this.settings.getProperty(Settings.ROOT_BASE_COLOR),
                this.settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)
        ));
        this.scrollPane.prefHeightProperty().bind(this.root.heightProperty());
        this.scrollPane.vvalueProperty().bind(this.container.heightProperty());
        startBot();
    }

    private void onTopInit() {
        setOnTopImage();
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
                    .setAutoReconnect(true)
                    .addListener(listener)
                    .addAutoJoinChannel("#" + connect.getProperty("channel"))
                    .buildConfiguration();
            bot = new PircBotX(config);
            try {
                bot.startBot();
            } catch (IOException | IrcException exception) {
                logger.error(exception.getMessage(), exception);
                throw new RuntimeException("Bot failed to start.\n " +
                        "Check properties in settings/twitch.properties and restart application.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void settingsOnAction() {
        final SettingsDialog dialog = new SettingsDialog();
        dialog.openDialog(getStage(), this.root);
        this.setting.setDisable(true);
    }

    public void onTopOnAction() {
        reverseOnTop();
        PlusChatFX.stage.setAlwaysOnTop(this.isOnTop);
        setOnTopImage();
        this.settings.setProperty(Settings.ROOT_ALWAYS_ON_TOP, String.valueOf(this.isOnTop));
        AppProperty.setProperties("./settings/settings.properties", this.settings);
    }

    private void reverseOnTop() {
        this.isOnTop = !this.isOnTop;
    }

    private void setOnTopImage() {
        final ImageView imageView = new ImageView(new Image(getOnTopImagePath()));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        this.onTop.setGraphic(imageView);
    }

    private String getOnTopImagePath() {
        String name = IMG_PIN_ENABLED_PNG_PATH;
        if (!this.isOnTop) {
            name = IMG_PIN_DISABLED_PNG_PATH;
        }
        return name;
    }

    @Override
    public void update(final String nick, final String message) {
        final TextFlow messageContainer = new TextFlow();
        String userName = nick;
        final Optional<User> userByName = this.userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            final User user = userByName.get();
            userName = user.getCustomName();
            final Label rankImage = getRankImage(user);
            addNodesToMessageContainer(messageContainer, rankImage);
        }
        addUserNameToMessageContainer(messageContainer, userName);
        addSeparatorToMessageContainer(messageContainer, ": ");
        addUserMessageToMessageContainer(messageContainer, message);
        this.messages.add(messageContainer);
        addNewMessageToContainer();
    }

    private void addNewMessageToContainer() {
        this.container.getChildren().add(this.messages.get(this.messageIndex++));
    }

    private void addSeparatorToMessageContainer(final TextFlow messageContainer, final String messageSeparator) {
        final Text separator = getText(messageSeparator, "separator", this.settings.getProperty(Settings.FONT_SEPARATOR_COLOR));
        addNodesToMessageContainer(messageContainer, separator);
    }

    private void addUserNameToMessageContainer(final TextFlow messageContainer, final String userName) {
        final Text nick = getText(userName, "user-name", this.settings.getProperty(Settings.FONT_NICK_COLOR));
        addNodesToMessageContainer(messageContainer, nick);
    }

    private void addUserMessageToMessageContainer(final TextFlow messageContainer, final String message) {
        final List<Node> messageNodes = getMessageNodes(message);
        messageNodes.iterator().forEachRemaining(node -> addNodesToMessageContainer(messageContainer, node));
    }

    private void addNodesToMessageContainer(final TextFlow textFlow, final Node... nodes) {
        textFlow.getChildren().addAll(nodes);
    }

    private List<Node> getMessageNodes(final String message) {
        final List<Node> nodes = new ArrayList<>();
        for (String word : getWords(message)) {
            final Optional<Smile> smileByName = this.smileRepository.getSmileByName(word);
            if (smileByName.isPresent()) {
                final Smile smile = smileByName.get();
                try {
                    nodes.add(getGraphicLabel(smile));
                } catch (FileNotFoundException exception) {
                    logger.error(exception.getMessage(), exception);
                    final Text text = getText(message, word);
                    nodes.add(text);
                }
            } else {
                final Text text = getText(message, word);
                nodes.add(text);
            }
        }
        return nodes;
    }

    private String[] getWords(final String message) {
        final String utf8Message = StringUtil.getUTF8String(message);
        return utf8Message.split(" ");
    }

    private Text getText(final String message, final String word) {
        final Text text = new Text(word + " ");
        final boolean isSoundEnable = Boolean.parseBoolean(this.settings.getProperty(Settings.SOUND_ENABLE));
        final String messageSound = this.settings.getProperty(Settings.SOUND_MESSAGE);
        final String directMessageSound = this.settings.getProperty(Settings.SOUND_DIRECT_MESSAGE);
        final double soundMessageVolume = Double.valueOf(this.settings.getProperty(Settings.SOUND_MESSAGE_VOLUME)) / 100;
        final double soundDirectMessageVolume = Double.valueOf(this.settings.getProperty(Settings.SOUND_DIRECT_MESSAGE_VOLUME)) / 100;
        if (isDirect(message)) {
            playSound("./sound/" + directMessageSound, isSoundEnable, soundDirectMessageVolume);
            text.setId("user-direct-message");
            text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty(Settings.FONT_SIZE), this.settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR)));
        } else {
            playSound("./sound/" + messageSound, isSoundEnable, soundMessageVolume);
            text.setId("user-message");
            text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty(Settings.FONT_SIZE), this.settings.getProperty(Settings.FONT_MESSAGE_COLOR)));
        }
        return text;
    }

    private void playSound(final String path, final boolean isSoundEnable, final double volume) {
        final Media sound = new Media(new File(path).toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setMute(!isSoundEnable);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    private Label getGraphicLabel(final Smile smile) throws FileNotFoundException {
        final Label image = new Label();
        final ImageView imageView = getImageView(smile.getImagePath());
        image.setGraphic(imageView);
        return image;
    }

    private boolean isDirect(final String message) {
        final Set<Direct> directs = this.directRepository.getAll();
        return directs
                .stream()
                .anyMatch((direct -> StringUtils.containsIgnoreCase(message, direct.getWord())));
    }

    private ImageView getImageView(final String path) throws FileNotFoundException {
        final FileInputStream fis = new FileInputStream(path);
        return new ImageView(new Image(fis));
    }

    private Label getRankImage(final User user) {
        final Label image = new Label();
        final Rank rank = this.rankRepository.getRankByExp(user.getExp());
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
        text.setStyle(StyleUtil.getTextStyle(this.settings.getProperty(Settings.FONT_SIZE), color));
        return text;
    }

    public void setSettings(final Properties settings) {
        this.settings = settings;
    }

    public Button getSetting() {
        return this.setting;
    }

    private Stage getStage() {
        return (Stage) this.container.getScene().getWindow();
    }
}
