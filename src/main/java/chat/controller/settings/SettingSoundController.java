package chat.controller.settings;

import chat.component.ChatDialog;
import chat.component.SettingsDialog;
import chat.controller.ApplicationStyle;
import chat.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingSoundController {

    private final static Logger logger = LogManager.getLogger(SettingSoundController.class);

    private AppProperty settingsProperties;
    private Paths paths;
    @FXML
    private CheckBox enableSoundCheckBox;
    @FXML
    private ChoiceBox<String> messageSoundChoiceBox;
    @FXML
    private Label messageVolumeValue;
    @FXML
    private Slider messageVolumeSlider;
    @FXML
    private Label directMessageVolumeValue;
    @FXML
    private Slider directMessageVolumeSlider;
    @FXML
    private ChoiceBox<String> directMessageSoundChoiceBox;
    private Properties settings;

    @Autowired
    public SettingSoundController(AppProperty settingsProperties, Paths paths) {
        this.settingsProperties = settingsProperties;
        this.paths = paths;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.getProperty();
        initEnableSound();
        initMessageSound();
        initMessageSoundVolume();
        initDirectMessageSound();
        initDirectMessageSoundVolume();
    }

    private void initEnableSound() {
        if (isSoundEnable()) {
            enableSoundCheckBox.setSelected(true);
        }
    }

    private boolean isSoundEnable() {
        return Boolean.parseBoolean(settings.getProperty(Settings.SOUND_ENABLE));
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

    public void saveSettings() {
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
    }
}
