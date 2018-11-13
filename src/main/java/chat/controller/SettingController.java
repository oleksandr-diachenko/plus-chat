package chat.controller;

import chat.component.ChatDialog;
import chat.component.ConfirmDialog;
import chat.component.DataDialog;
import chat.component.RandomizerDialog;
import chat.model.entity.*;
import chat.model.repository.*;
import chat.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Controller
@NoArgsConstructor
public class SettingController {

    private final static Logger logger = LogManager.getLogger(SettingController.class);

    @FXML
    private Label directMessageVolumeValue;
    @FXML
    private Slider directMessageVolumeSlider;
    @FXML
    private ChoiceBox<String> directMessageSoundChoiceBox;
    @FXML
    private Label messageVolumeValue;
    @FXML
    private Slider messageVolumeSlider;
    @FXML
    private ChoiceBox<String> messageSoundChoiceBox;
    @FXML
    private CheckBox enableSoundCheckBox;
    @FXML
    private ColorPicker directMessageColorPicker;
    @FXML
    private ColorPicker baseColorPicker;
    @FXML
    private Node settingsRoot;
    @FXML
    private Label transparencyValue;
    @FXML
    private Label fontSize;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private Slider fontSizeSlider;
    @FXML
    private Slider transparencySlider;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker nickColorPicker;
    @FXML
    private ColorPicker separatorColorPicker;
    @FXML
    private ColorPicker messageColorPicker;
    private Properties settings;
    private Map<String, String> languages;
    private Node chatRoot;
    private ApplicationStyle applicationStyle;
    private StyleUtil styleUtil;
    private ConfirmController confirmController;
    private ChatDialog chatDialog;
    private RandomizerDialog randomizerDialog;
    private AppProperty settingsProperties;
    private ConfirmDialog confirmDialog;
    private DataDialog dataDialog;
    private Paths paths;
    private CommandRepository commandRepository;
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private SmileRepository smileRepository;
    private DirectRepository directRepository;

    @Autowired
    public SettingController(AppProperty settingsProperties, ConfirmDialog confirmDialog,
                             DataDialog dataDialog, Paths paths,
                             CommandRepository commandRepository,
                             UserRepository userRepository, RankRepository rankRepository,
                             SmileRepository smileRepository, DirectRepository directRepository,
                             ApplicationStyle applicationStyle, StyleUtil styleUtil,
                             ConfirmController confirmController, ChatDialog chatDialog,
                             RandomizerDialog randomizerDialog) {
        this.settingsProperties = settingsProperties;
        this.confirmDialog = confirmDialog;
        this.dataDialog = dataDialog;
        this.paths = paths;
        this.commandRepository = commandRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.smileRepository = smileRepository;
        this.directRepository = directRepository;
        this.applicationStyle = applicationStyle;
        this.styleUtil = styleUtil;
        this.confirmController = confirmController;
        this.chatDialog = chatDialog;
        this.randomizerDialog = randomizerDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.getProperty();
        settingsRoot.setStyle(styleUtil.getRootStyle(settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
        chatRoot = getChatRoot();
        initLanguage();
        initTheme();
        initFontSizeSlider();
        initTransparencySlider();
        initBaseColorPicker();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
        initDirectMessageColorPicker();
        initEnableSound();
        initMessageSound();
        initMessageSoundVolume();
        initDirectMessageSound();
        initDirectMessageSoundVolume();
    }

    private boolean isSoundEnable() {
        return Boolean.parseBoolean(settings.getProperty(Settings.SOUND_ENABLE));
    }

    private void initBaseColorPicker() {
        baseColorPicker.setValue(Color.valueOf(settings.getProperty(Settings.ROOT_BASE_COLOR)));
        baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBaseColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initLanguage() {
        languages = new HashMap<>();
        languages.put("en", "English");
        languages.put("ru", "Russian");
        languages.put("ua", "Ukrainian");
        languageChoiceBox.setItems(FXCollections.observableArrayList(languages.values()));
        languageChoiceBox.setValue(languages.get(
                settings.getProperty(Settings.ROOT_LANGUAGE)));
    }

    private void initTheme() {
        List<String> list = new ArrayList<>();
        list.add("default");
        themeChoiceBox.setItems(FXCollections.observableArrayList(list));
        themeChoiceBox.setValue(settings.getProperty(Settings.ROOT_THEME));
    }

    private void initFontSizeSlider() {
        String fontSizeValue = settings.getProperty(Settings.FONT_SIZE);
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            applicationStyle.setFontSize(String.valueOf(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initTransparencySlider() {
        String backgroundTransparencyValue = settings.getProperty(
                Settings.ROOT_BACKGROUND_TRANSPARENCY);
        transparencyValue.setText(backgroundTransparencyValue);
        transparencySlider.setValue(Long.valueOf(backgroundTransparencyValue));
        transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            getOwner().setOpacity((Double) new_val / 100);
            transparencyValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initBackGroundColorPicker() {
        backgroundColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.ROOT_BACKGROUND_COLOR)));
        backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBackgroundColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initNickColorPicker() {
        nickColorPicker.setValue(Color.valueOf(settings.getProperty(Settings.FONT_NICK_COLOR)));
        nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setNickColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initSeparatorColorPicker() {
        separatorColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_SEPARATOR_COLOR)));
        separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setSeparatorColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initMessageColorPicker() {
        messageColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_MESSAGE_COLOR)));
        messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setMessageColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initDirectMessageColorPicker() {
        directMessageColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_DIRECT_MESSAGE_COLOR)));
        directMessageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setDirectColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(chatRoot, settingsRoot, applicationStyle);
        });
    }

    private void initEnableSound() {
        if (isSoundEnable()) {
            enableSoundCheckBox.setSelected(true);
        }
    }

    private void initMessageSound() {
        try {
            Set<File> sounds = FileUtil.getFilesFromFolder("./sound/");
            Set<String> soundNames = new HashSet<>();
            sounds.forEach(sound -> soundNames.add(sound.getName()));
            messageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
            messageSoundChoiceBox.setValue(settings.getProperty(Settings.SOUND_MESSAGE));
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Sound directory not found.\n " +
                    "Put your sounds to " + paths.getSoundsDirectory() +
                    " and restart application.", exception);
        }
    }

    private void initMessageSoundVolume() {
        String messageSoundVolumeValue = settings.getProperty(Settings.SOUND_MESSAGE_VOLUME);
        messageVolumeValue.setText(messageSoundVolumeValue);
        messageVolumeSlider.setValue(Double.parseDouble(messageSoundVolumeValue));
        messageVolumeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            messageVolumeValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initDirectMessageSound() {
        try {
            Set<File> sounds = FileUtil.getFilesFromFolder("./sound/");
            Set<String> soundNames = new HashSet<>();
            sounds.forEach(sound -> soundNames.add(sound.getName()));
            directMessageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
            directMessageSoundChoiceBox.setValue(settings.getProperty(
                    Settings.SOUND_DIRECT_MESSAGE));
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Sound directory not found.\n " +
                    "Put your sounds to " + paths.getSoundsDirectory() +
                    " and restart application.", exception);
        }
    }

    private void initDirectMessageSoundVolume() {
        String directMessageSoundVolumeValue = settings.getProperty(
                Settings.SOUND_DIRECT_MESSAGE_VOLUME);
        directMessageVolumeValue.setText(directMessageSoundVolumeValue);
        directMessageVolumeSlider.setValue(Double.parseDouble(directMessageSoundVolumeValue));
        directMessageVolumeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            directMessageVolumeValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    public void reloadAction() {
        commandRepository.getAll();
        directRepository.getAll();
        rankRepository.getAll();
        smileRepository.getAll();
        userRepository.getAll();
    }

    public void confirmAction() {
        confirmDialog.openDialog(getStage());
        Stage stage = confirmDialog.getStage();
        stage.setOnCloseRequest(event -> {
            if (confirmController.isConfirmed()) {
                flushSettings();
            }
        });
    }

    public void cancelAction() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void flushSettings() {
        settings.setProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY, transparencyValue.getText());
        settings.setProperty(Settings.FONT_SIZE, fontSize.getText());
        settings.setProperty(Settings.ROOT_LANGUAGE, getLanguage(languageChoiceBox.getValue()));
        settings.setProperty(Settings.ROOT_THEME, themeChoiceBox.getValue());

        settings.setProperty(Settings.ROOT_BASE_COLOR, ColorUtil.getHexColor(
                baseColorPicker.getValue()));
        settings.setProperty(Settings.ROOT_BACKGROUND_COLOR, ColorUtil.getHexColor(
                backgroundColorPicker.getValue()));
        settings.setProperty(Settings.FONT_NICK_COLOR, ColorUtil.getHexColor(
                nickColorPicker.getValue()));
        settings.setProperty(Settings.FONT_SEPARATOR_COLOR, ColorUtil.getHexColor(
                separatorColorPicker.getValue()));
        settings.setProperty(Settings.FONT_MESSAGE_COLOR, ColorUtil.getHexColor(
                messageColorPicker.getValue()));
        settings.setProperty(Settings.FONT_DIRECT_MESSAGE_COLOR, ColorUtil.getHexColor(
                directMessageColorPicker.getValue()));
        settings.setProperty(Settings.SOUND_ENABLE, String.valueOf(
                enableSoundCheckBox.isSelected()));
        settings.setProperty(Settings.SOUND_MESSAGE,
                messageSoundChoiceBox.getValue());
        settings.setProperty(Settings.SOUND_MESSAGE_VOLUME,
                messageVolumeValue.getText());
        settings.setProperty(Settings.SOUND_DIRECT_MESSAGE,
                directMessageSoundChoiceBox.getValue());
        settings.setProperty(Settings.SOUND_DIRECT_MESSAGE_VOLUME,
                directMessageVolumeValue.getText());

        settingsProperties.setProperties(settings);
        ChatController chatController = (ChatController) chatRoot.getUserData();
        chatController.setSettings(settings);
        chatController.getSetting().setDisable(false);
    }

    private String getLanguage(String value) {
        for (String key : languages.keySet()) {
            if (languages.get(key).equals(value)) {
                return key;
            }
        }
        return "en";
    }

    public void commandsDataAction() {
        Set<Command> commands = commandRepository.getAll();
        Set<String> fields = getFields(Command.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void usersDataAction() {
        Set<User> commands = userRepository.getAll();
        Set<String> fields = getFields(User.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void ranksDataAction() {
        Set<Rank> ranks = rankRepository.getAll();
        Set<String> fields = getFields(Rank.class.getDeclaredFields());
        openDialog(new HashSet<>(ranks), fields);
    }

    public void smilesDataAction() {
        Set<Smile> smiles = smileRepository.getAll();
        Set<String> fields = getFields(Smile.class.getDeclaredFields());
        openDialog(new HashSet<>(smiles), fields);
    }

    public void directsDataAction() {
        Set<Direct> directs = directRepository.getAll();
        Set<String> fields = getFields(Direct.class.getDeclaredFields());
        openDialog(new HashSet<>(directs), fields);
    }

    private Set<String> getFields(Field[] declaredFields) {
        Set<String> fields = new HashSet<>();
        for (Field field : declaredFields) {
            fields.add(field.getName());
        }
        return fields;
    }

    private void openDialog(Set<Object> data, Set<String> fields) {
        dataDialog.setTableFields(fields);
        dataDialog.setTableData(data);
        dataDialog.openDialog(getStage());
    }

    private Stage getStage() {
        return (Stage) settingsRoot.getScene().getWindow();
    }

    private Stage getOwner() {
        return chatDialog.getStage();
    }

    public Node getRoot() {
        return settingsRoot;
    }

    private Node getChatRoot() {
        Stage owner = getOwner();
        return owner.getScene().lookup("#root");
    }

    public void randomAction() {
        randomizerDialog.openDialog(getStage());
    }
}
