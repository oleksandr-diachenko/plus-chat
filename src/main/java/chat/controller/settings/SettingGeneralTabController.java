package chat.controller.settings;

import chat.component.CustomButton;
import chat.component.CustomStage;
import chat.component.dialog.ChatDialog;
import chat.component.dialog.RandomizerDialog;
import chat.controller.Customizable;
import chat.util.AppProperty;
import chat.util.Settings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingGeneralTabController implements Customizable {

    @FXML
    private CustomButton randomizer;
    @FXML
    private ChoiceBox<String> languageChoiceBox;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private Slider transparencySlider;
    @FXML
    private Label transparencyValue;
    private AppProperty settingsProperties;
    private ChatDialog chatDialog;
    private RandomizerDialog randomizerDialog;
    private Properties settings;
    private Map<String, String> languages;

    @Autowired
    public SettingGeneralTabController(AppProperty settingsProperties, ChatDialog chatDialog,
                                       RandomizerDialog randomizerDialog) {
        this.settingsProperties = settingsProperties;
        this.chatDialog = chatDialog;
        this.randomizerDialog = randomizerDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.loadProperty();
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
        randomizer.setDisable(true);
        CustomStage stage = new CustomStage();
        randomizerDialog.openDialog(stage);
        stage.setOnCloseRequest(event -> randomizer.setDisable(false));
    }

    private String getLanguage(String value) {
        for (String key : languages.keySet()) {
            if (languages.get(key).equals(value)) {
                return key;
            }
        }
        return "en";
    }

    private CustomStage getOwner() {
        return chatDialog.getStage();
    }

    @Override
    public void customize(Properties properties) {
        properties.setProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY, transparencyValue.getText());
        properties.setProperty(Settings.ROOT_LANGUAGE, getLanguage(languageChoiceBox.getValue()));
        properties.setProperty(Settings.ROOT_THEME, themeChoiceBox.getValue());
        settingsProperties.storeProperties(properties);
    }
}
