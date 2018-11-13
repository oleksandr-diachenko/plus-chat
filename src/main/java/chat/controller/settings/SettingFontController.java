package chat.controller.settings;

import chat.component.ChatDialog;
import chat.component.SettingsDialog;
import chat.controller.ApplicationStyle;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingFontController {

    private AppProperty settingsProperties;
    private ApplicationStyle applicationStyle;
    private StyleUtil styleUtil;
    private ChatDialog chatDialog;
    @FXML
    private Label fontSize;
    @FXML
    private Slider fontSizeSlider;
    private Properties settings;
    private SettingsDialog settingsDialog;
    private Node settingsRoot;

    @Autowired
    public SettingFontController(AppProperty settingsProperties, ApplicationStyle applicationStyle,
                                 StyleUtil styleUtil, ChatDialog chatDialog,
                                 SettingsDialog settingsDialog) {
        this.settingsProperties = settingsProperties;
        this.applicationStyle = applicationStyle;
        this.styleUtil = styleUtil;
        this.chatDialog = chatDialog;
        this.settingsDialog = settingsDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.getProperty();
        initFontSizeSlider();
    }

    private void initFontSizeSlider() {
        String fontSizeValue = settings.getProperty(Settings.FONT_SIZE);
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            applicationStyle.setFontSize(String.valueOf(new_val));
            styleUtil.setStyles(getChatRoot(), settingsRoot, applicationStyle);
        });
    }

    private Node getSettingsRoot() {
        Stage owner = settingsDialog.getStage();
        return owner.getScene().lookup("#root");
    }

    private Node getChatRoot() {
        Stage owner = chatDialog.getStage();
        return owner.getScene().lookup("#root");
    }

    public void saveSettings(Properties settings) {
        settings.setProperty(Settings.FONT_SIZE, fontSize.getText());
        settingsProperties.setProperties(settings);
    }

    public void setSettingsRoot(Node settingsRoot) {
        this.settingsRoot = settingsRoot;
    }
}
