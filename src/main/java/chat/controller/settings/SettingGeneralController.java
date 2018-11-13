package chat.controller.settings;

import chat.component.ChatDialog;
import chat.component.RandomizerDialog;
import chat.util.AppProperty;
import chat.util.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingGeneralController {

    private AppProperty settingsProperties;
    private ChatDialog chatDialog;
    private final RandomizerDialog randomizerDialog;
    @FXML
    private GridPane settingsGeneralRoot;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private Slider transparencySlider;
    @FXML
    private Label transparencyValue;
    private Properties settings;
    private Map<String, String> languages;

    @Autowired
    public SettingGeneralController(AppProperty settingsProperties, ChatDialog chatDialog,
                                    RandomizerDialog randomizerDialog) {
        this.settingsProperties = settingsProperties;
        this.chatDialog = chatDialog;
        this.randomizerDialog = randomizerDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.getProperty();
        initLanguage();
        initTheme();
        initTransparencySlider();
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

    public void randomAction() {
        randomizerDialog.openDialog(getStage());
    }

    private String getLanguage(String value) {
        for (String key : languages.keySet()) {
            if (languages.get(key).equals(value)) {
                return key;
            }
        }
        return "en";
    }

    private Stage getStage() {
        return (Stage) settingsGeneralRoot.getScene().getWindow();
    }

    private Stage getOwner() {
        return chatDialog.getStage();
    }

    public void saveSettings() {
        settings.setProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY, transparencyValue.getText());
        settings.setProperty(Settings.ROOT_LANGUAGE, getLanguage(languageChoiceBox.getValue()));
        settings.setProperty(Settings.ROOT_THEME, themeChoiceBox.getValue());
        settingsProperties.setProperties(settings);
    }
}
