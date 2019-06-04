package chat.controller.settings;

import chat.util.AppProperty;
import chat.util.FileUtil;
import chat.util.Paths;
import chat.util.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class SettingSoundController {

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
    private AppProperty settingsProperties;
    private Paths paths;
    private Properties settings;

    @Autowired
    public SettingSoundController(AppProperty settingsProperties, Paths paths) {
        this.settingsProperties = settingsProperties;
        this.paths = paths;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.loadProperty();
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
        Set<String> soundNames = new HashSet<>();
        getSounds().forEach(sound -> soundNames.add(sound.getName()));
        messageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
        messageSoundChoiceBox.setValue(settings.getProperty(Settings.SOUND_MESSAGE));
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
        Set<String> soundNames = new HashSet<>();
        getSounds().forEach(sound -> soundNames.add(sound.getName()));
        directMessageSoundChoiceBox.setItems(FXCollections.observableArrayList(soundNames));
        directMessageSoundChoiceBox.setValue(settings.getProperty(
                Settings.SOUND_DIRECT_MESSAGE));
    }

    private Set<File> getSounds() {
        try {
            return FileUtil.getFilesFromFolder(paths.getSoundsDirectory());
        } catch (FileNotFoundException exception) {
            log.error(exception.getMessage(), exception);
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

    void storeSettingProperty(Properties settings) {
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
        settingsProperties.storeProperties(settings);
    }
}
