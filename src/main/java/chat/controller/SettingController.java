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
        this.settings = settingsProperties.getProperty();
        this.settingsRoot.setStyle(this.styleUtil.getRootStyle(settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
        this.chatRoot = getChatRoot();
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
        return Boolean.parseBoolean(this.settings.getProperty(Settings.SOUND_ENABLE));
    }

    private void initBaseColorPicker() {
        this.baseColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.ROOT_BASE_COLOR)));
        this.baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setBaseColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initLanguage() {
        this.languages = new HashMap<>();
        this.languages.put("en", "English");
        this.languages.put("ru", "Russian");
        this.languages.put("ua", "Ukrainian");
        this.languageChoiceBox.setItems(FXCollections.observableArrayList(this.languages.values()));
        this.languageChoiceBox.setValue(this.languages.get(
                this.settings.getProperty(Settings.ROOT_LANGUAGE)));
    }

    private void initTheme() {
        List<String> list = new ArrayList<>();
        list.add("default");
        this.themeChoiceBox.setItems(FXCollections.observableArrayList(list));
        this.themeChoiceBox.setValue(this.settings.getProperty(Settings.ROOT_THEME));
    }

    private void initFontSizeSlider() {
        String fontSizeValue = this.settings.getProperty(Settings.FONT_SIZE);
        this.fontSize.setText(fontSizeValue);
        this.fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        this.fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            this.fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            this.applicationStyle.setFontSize(String.valueOf(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initTransparencySlider() {
        String backgroundTransparencyValue = this.settings.getProperty(
                Settings.ROOT_BACKGROUND_TRANSPARENCY);
        this.transparencyValue.setText(backgroundTransparencyValue);
        this.transparencySlider.setValue(Long.valueOf(backgroundTransparencyValue));
        this.transparencySlider.valueProperty().addListener((ov, old_val, new_val) -> {
            getOwner().setOpacity((Double) new_val / 100);
            this.transparencyValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initBackGroundColorPicker() {
        this.backgroundColorPicker.setValue(Color.valueOf(this.settings.getProperty(
                Settings.ROOT_BACKGROUND_COLOR)));
        this.backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setBackgroundColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initNickColorPicker() {
        this.nickColorPicker.setValue(Color.valueOf(this.settings.getProperty(Settings.FONT_NICK_COLOR)));
        this.nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setNickColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initSeparatorColorPicker() {
        this.separatorColorPicker.setValue(Color.valueOf(this.settings.getProperty(
                Settings.FONT_SEPARATOR_COLOR)));
        this.separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setSeparatorColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initMessageColorPicker() {
        this.messageColorPicker.setValue(Color.valueOf(this.settings.getProperty(
                Settings.FONT_MESSAGE_COLOR)));
        this.messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setMessageColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initDirectMessageColorPicker() {
        this.directMessageColorPicker.setValue(Color.valueOf(this.settings.getProperty(
                Settings.FONT_DIRECT_MESSAGE_COLOR)));
        this.directMessageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            this.applicationStyle.setDirectColor(ColorUtil.getHexColor(new_val));
            this.styleUtil.setStyles(this.chatRoot, this.settingsRoot, this.applicationStyle);
        });
    }

    private void initEnableSound() {
        if (isSoundEnable()) {
            this.enableSoundCheckBox.setSelected(true);
        }
    }

    private void initMessageSound() {
        try {
            Set<File> sounds = FileUtil.getFilesFromFolder("./sound/");
            Set<String> soundNames = new HashSet<>();
            sounds.forEach(sound -> soundNames.add(sound.getName()));
            this.messageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
            this.messageSoundChoiceBox.setValue(this.settings.getProperty(Settings.SOUND_MESSAGE));
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Sound directory not found.\n " +
                    "Put your sounds to " + this.paths.getSoundsDirectory() +
                    " and restart application.", exception);
        }
    }

    private void initMessageSoundVolume() {
        String messageSoundVolumeValue = this.settings.getProperty(Settings.SOUND_MESSAGE_VOLUME);
        this.messageVolumeValue.setText(messageSoundVolumeValue);
        this.messageVolumeSlider.setValue(Double.parseDouble(messageSoundVolumeValue));
        this.messageVolumeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            this.messageVolumeValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    private void initDirectMessageSound() {
        try {
            Set<File> sounds = FileUtil.getFilesFromFolder("./sound/");
            Set<String> soundNames = new HashSet<>();
            sounds.forEach(sound -> soundNames.add(sound.getName()));
            this.directMessageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
            this.directMessageSoundChoiceBox.setValue(this.settings.getProperty(
                    Settings.SOUND_DIRECT_MESSAGE));
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Sound directory not found.\n " +
                    "Put your sounds to " + this.paths.getSoundsDirectory() +
                    " and restart application.", exception);
        }
    }

    private void initDirectMessageSoundVolume() {
        String directMessageSoundVolumeValue = this.settings.getProperty(
                Settings.SOUND_DIRECT_MESSAGE_VOLUME);
        this.directMessageVolumeValue.setText(directMessageSoundVolumeValue);
        this.directMessageVolumeSlider.setValue(Double.parseDouble(directMessageSoundVolumeValue));
        this.directMessageVolumeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            this.directMessageVolumeValue.setText(String.valueOf(Math.round(new_val.doubleValue())));
        });
    }

    public void reloadAction() {
        this.commandRepository.getAll();
        this.directRepository.getAll();
        this.rankRepository.getAll();
        this.smileRepository.getAll();
        this.userRepository.getAll();
    }

    public void confirmAction() {
        this.confirmDialog.openDialog(getStage());
        Stage stage = confirmDialog.getStage();
        stage.setOnCloseRequest(event -> {
            if (this.confirmController.isConfirmed()) {
                flushSettings();
            }
        });
    }

    public void cancelAction() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void flushSettings() {
        this.settings.setProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY, this.transparencyValue.getText());
        this.settings.setProperty(Settings.FONT_SIZE, this.fontSize.getText());
        this.settings.setProperty(Settings.ROOT_LANGUAGE, getLanguage(this.languageChoiceBox.getValue()));
        this.settings.setProperty(Settings.ROOT_THEME, this.themeChoiceBox.getValue());

        this.settings.setProperty(Settings.ROOT_BASE_COLOR, ColorUtil.getHexColor(
                this.baseColorPicker.getValue()));
        this.settings.setProperty(Settings.ROOT_BACKGROUND_COLOR, ColorUtil.getHexColor(
                this.backgroundColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_NICK_COLOR, ColorUtil.getHexColor(
                this.nickColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_SEPARATOR_COLOR, ColorUtil.getHexColor(
                this.separatorColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_MESSAGE_COLOR, ColorUtil.getHexColor(
                this.messageColorPicker.getValue()));
        this.settings.setProperty(Settings.FONT_DIRECT_MESSAGE_COLOR, ColorUtil.getHexColor(
                this.directMessageColorPicker.getValue()));
        this.settings.setProperty(Settings.SOUND_ENABLE, String.valueOf(
                this.enableSoundCheckBox.isSelected()));
        this.settings.setProperty(Settings.SOUND_MESSAGE,
                this.messageSoundChoiceBox.getValue());
        this.settings.setProperty(Settings.SOUND_MESSAGE_VOLUME,
                this.messageVolumeValue.getText());
        this.settings.setProperty(Settings.SOUND_DIRECT_MESSAGE,
                this.directMessageSoundChoiceBox.getValue());
        this.settings.setProperty(Settings.SOUND_DIRECT_MESSAGE_VOLUME,
                this.directMessageVolumeValue.getText());

        this.settingsProperties.setProperties(this.settings);
        ChatController chatController = (ChatController) this.chatRoot.getUserData();
        chatController.setSettings(this.settings);
        chatController.getSetting().setDisable(false);
    }

    private String getLanguage(String value) {
        for (String key : this.languages.keySet()) {
            if (this.languages.get(key).equals(value)) {
                return key;
            }
        }
        return "en";
    }

    public void commandsDataAction() {
        Set<Command> commands = this.commandRepository.getAll();
        Set<String> fields = getFields(Command.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void usersDataAction() {
        Set<User> commands = this.userRepository.getAll();
        Set<String> fields = getFields(User.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void ranksDataAction() {
        Set<Rank> ranks = this.rankRepository.getAll();
        Set<String> fields = getFields(Rank.class.getDeclaredFields());
        openDialog(new HashSet<>(ranks), fields);
    }

    public void smilesDataAction() {
        Set<Smile> smiles = this.smileRepository.getAll();
        Set<String> fields = getFields(Smile.class.getDeclaredFields());
        openDialog(new HashSet<>(smiles), fields);
    }

    public void directsDataAction() {
        Set<Direct> directs = this.directRepository.getAll();
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
        this.dataDialog.setTableFields(fields);
        this.dataDialog.setTableData(data);
        this.dataDialog.openDialog(getStage());
    }

    private Stage getStage() {
        return (Stage) settingsRoot.getScene().getWindow();
    }

    private Stage getOwner() {
        return this.chatDialog.getStage();
    }

    public Node getRoot() {
        return this.settingsRoot;
    }

    private Node getChatRoot() {
        Stage owner = getOwner();
        return owner.getScene().lookup("#root");
    }

    public void randomAction() {
        this.randomizerDialog.openDialog(getStage());
    }
}
