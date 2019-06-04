package chat.controller;

import chat.component.SettingsDialog;
import chat.model.entity.Direct;
import chat.model.entity.Rank;
import chat.model.entity.Smile;
import chat.model.entity.User;
import chat.model.repository.DirectRepository;
import chat.model.repository.RankRepository;
import chat.model.repository.SmileRepository;
import chat.model.repository.UserRepository;
import chat.observer.Observer;
import chat.sevice.Bot;
import chat.util.*;
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
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Controller
@NoArgsConstructor
@Log4j2
public class ChatController implements Observer {

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
    private SmileRepository smileRepository;
    private DirectRepository directRepository;
    private int messageIndex = 0;
    private boolean isOnTop;
    private AppProperty settingsProperties;
    private AppProperty twitchProperties;
    private SettingsDialog settingsDialog;
    private Paths paths;
    private StyleUtil styleUtil;
    @Autowired
    private Bot listener;


    @Autowired
    public ChatController(RankRepository rankRepository, UserRepository userRepository, SmileRepository smileRepository,
                          DirectRepository directRepository,
                          @Qualifier("settingsProperties") AppProperty settingsProperties,
                          @Qualifier("twitchProperties") AppProperty twitchProperties,
                          SettingsDialog settingsDialog, Paths paths, StyleUtil styleUtil) {
        this.rankRepository = rankRepository;
        this.userRepository = userRepository;
        this.smileRepository = smileRepository;
        this.directRepository = directRepository;
        this.settingsProperties = settingsProperties;
        this.twitchProperties = twitchProperties;
        this.settingsDialog = settingsDialog;
        this.paths = paths;
        this.styleUtil = styleUtil;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.loadProperty();
        isOnTop = Boolean.parseBoolean(settings.getProperty(Settings.ROOT_ALWAYS_ON_TOP));
        onTopInit();
        root.setStyle(styleUtil.getRootStyle(
                settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)
        ));
        scrollPane.prefHeightProperty().bind(root.heightProperty());
        scrollPane.vvalueProperty().bind(container.heightProperty());
        startBot();
    }

    private void onTopInit() {
        setOnTopImage();
    }

    private void startBot() {
        Thread thread = new Thread(() -> {
            Properties connect = twitchProperties.loadProperty();
            listener.addObserver(this);
            Configuration config = new Configuration.Builder()
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
                log.error(exception.getMessage(), exception);
                throw new RuntimeException("Bot failed to start.\n " +
                        "Check properties in " + paths.getTwitchProperties() + " " +
                        "and restart application.", exception);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void settingsOnAction() {
        settingsDialog.openDialog(getStage());
        setting.setDisable(true);
    }

    public void onTopOnAction() {
        reverseOnTop();

        getStage().setAlwaysOnTop(isOnTop);
        setOnTopImage();
        settings.setProperty(Settings.ROOT_ALWAYS_ON_TOP, String.valueOf(isOnTop));
        settingsProperties.storeProperties(settings);
    }

    private void reverseOnTop() {
        isOnTop = !isOnTop;
    }

    private void setOnTopImage() {
        ImageView imageView = new ImageView(new Image(getOnTopImagePath()));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        onTop.setGraphic(imageView);
    }

    private String getOnTopImagePath() {
        String name = paths.getEnabledPin();
        if (!isOnTop) {
            name = paths.getDisabledPin();
        }
        return name;
    }

    @Override
    public void update(String nick, String message) {
        TextFlow messageContainer = new TextFlow();
        String userName = nick;
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isPresent()) {
            User user = userByName.get();
            userName = user.getCustomName();
            Label rankImage = getRankImage(user);
            addNodesToMessageContainer(messageContainer, rankImage);
        }
        addUserNameToMessageContainer(messageContainer, userName);
        addSeparatorToMessageContainer(messageContainer, ": ");
        addUserMessageToMessageContainer(messageContainer, message);
        messages.add(messageContainer);
        addNewMessageToContainer();
        playSound(message);
    }

    private void addNewMessageToContainer() {
        if (messages.size() > Integer.parseInt(settings.getProperty(Settings.MESSAGE_MAX_DISPLAYED))) {
            messages.remove(0);
            messageIndex--;
            container.getChildren().remove(0);
        }
        container.getChildren().add(messages.get(messageIndex++));
    }

    private void addSeparatorToMessageContainer(TextFlow messageContainer, String messageSeparator) {
        Text separator = getText(messageSeparator, "separator",
                settings.getProperty(Settings.FONT_SEPARATOR_COLOR));
        addNodesToMessageContainer(messageContainer, separator);
    }

    private void addUserNameToMessageContainer(TextFlow messageContainer, String userName) {
        Text nick = getText(userName, "user-name",
                settings.getProperty(Settings.FONT_NICK_COLOR));
        addNodesToMessageContainer(messageContainer, nick);
    }

    private void addUserMessageToMessageContainer(TextFlow messageContainer, String message) {
        List<Node> messageNodes = getMessageNodes(message);
        messageNodes.forEach(node -> addNodesToMessageContainer(messageContainer, node));
    }

    private void addNodesToMessageContainer(TextFlow textFlow, Node... nodes) {
        textFlow.getChildren().addAll(nodes);
    }

    private void playSound(String message) {
        if (isDirect(message)) {
            play(Settings.SOUND_DIRECT_MESSAGE, Settings.SOUND_DIRECT_MESSAGE_VOLUME);
        } else {
            play(Settings.SOUND_MESSAGE, Settings.SOUND_MESSAGE_VOLUME);
        }
    }

    private void play(String sound, String soundVolume) {
        String messageSound = settings.getProperty(sound);
        double soundDirectMessageVolume = Double.valueOf(settings.getProperty(soundVolume)) / 100;
        playSound(paths.getSoundsDirectory() + messageSound, soundDirectMessageVolume);
    }

    private List<Node> getMessageNodes(String message) {
        boolean isDirect = isDirect(message);
        List<Node> nodes = new ArrayList<>();
        for (String word : getWords(message)) {
            Optional<Smile> smileByName = smileRepository.getSmileByName(word);
            if (smileByName.isPresent()) {
                nodes.add(getSmileLabel(smileByName.get()));
            } else {
                nodes.add(getText(word + " ", getWordId(isDirect), getWordColor(isDirect)));
            }
        }
        return nodes;
    }

    private String getWordColor(boolean isDirect) {
        if (isDirect) {
            return settings.getProperty(Settings.FONT_DIRECT_MESSAGE_COLOR);
        }
        return settings.getProperty(Settings.FONT_MESSAGE_COLOR);
    }

    private String getWordId(boolean isDirect) {
        if (isDirect) {
            return "user-direct-message";
        }
        return "user-message";
    }

    private String[] getWords(String message) {
        return message.split(" ");
    }

    private void playSound(String path, double volume) {
        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        boolean isSoundEnable = Boolean.parseBoolean(settings.getProperty(Settings.SOUND_ENABLE));
        mediaPlayer.setMute(!isSoundEnable);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    private Label getSmileLabel(Smile smile) {
        Label image = new Label();
        try (FileInputStream fis = new FileInputStream(smile.getImagePath())) {
            ImageView imageView = new ImageView(new Image(fis));
            image.setGraphic(imageView);
            return image;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            return new Label(smile.getName());
        }
    }

    private boolean isDirect(String message) {
        Set<Direct> directs = directRepository.getAll();
        for (Direct direct : directs) {
            String word = direct.getWord();
            if (StringUtils.containsIgnoreCase(message, word)) {
                return true;
            }
        }
        return false;
    }

    private Label getRankImage(User user) {
        Label image = new Label();
        Rank rank = rankRepository.getRankByExp(user.getExp());
        image.setId("rank-image");
        try (FileInputStream fis = new FileInputStream(rank.getImagePath())) {
            ImageView imageView = new ImageView(new Image(fis));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            image.setGraphic(imageView);
            return image;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return new Label();
    }

    private Text getText(String string, String id, String color) {
        Text text = new Text(StringUtil.getUTF8String(string));
        text.setId(id);
        text.setStyle(styleUtil.getTextStyle(settings.getProperty(Settings.FONT_SIZE), color));
        return text;
    }

    public void storeSettingProperty(Properties settings) {
        this.settings = settings;
    }

    public Button getSetting() {
        return setting;
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }

    public Bot getListener() {
        return listener;
    }
}
