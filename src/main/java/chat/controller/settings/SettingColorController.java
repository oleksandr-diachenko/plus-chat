package chat.controller.settings;

import chat.component.ChatDialog;
import chat.component.SettingsDialog;
import chat.controller.ApplicationStyle;
import chat.util.AppProperty;
import chat.util.ColorUtil;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingColorController {

    private AppProperty settingsProperties;
    private ChatDialog chatDialog;
    private ApplicationStyle applicationStyle;
    private StyleUtil styleUtil;
    private SettingsDialog settingsDialog;
    @FXML
    private ColorPicker baseColorPicker;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker nickColorPicker;
    @FXML
    private ColorPicker separatorColorPicker;
    @FXML
    private ColorPicker messageColorPicker;
    @FXML
    public ColorPicker directMessageColorPicker;
    private Properties settings;

    @Autowired
    public SettingColorController(AppProperty settingsProperties, ChatDialog chatDialog,
                                  ApplicationStyle applicationStyle,
                                  StyleUtil styleUtil, SettingsDialog settingsDialog) {
        this.settingsProperties = settingsProperties;
        this.chatDialog = chatDialog;
        this.applicationStyle = applicationStyle;
        this.styleUtil = styleUtil;
        this.settingsDialog = settingsDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.getProperty();
        initBaseColorPicker();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
        initDirectMessageColorPicker();
    }

    private void initBaseColorPicker() {
        baseColorPicker.setValue(Color.valueOf(settings.getProperty(Settings.ROOT_BASE_COLOR)));
        baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBaseColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
        });
    }

    private void initBackGroundColorPicker() {
        backgroundColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.ROOT_BACKGROUND_COLOR)));
        backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBackgroundColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
        });
    }

    private void initNickColorPicker() {
        nickColorPicker.setValue(Color.valueOf(settings.getProperty(Settings.FONT_NICK_COLOR)));
        nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setNickColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
        });
    }

    private void initSeparatorColorPicker() {
        separatorColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_SEPARATOR_COLOR)));
        separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setSeparatorColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
        });
    }

    private void initMessageColorPicker() {
        messageColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_MESSAGE_COLOR)));
        messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setMessageColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
        });
    }

    private void initDirectMessageColorPicker() {
        directMessageColorPicker.setValue(Color.valueOf(settings.getProperty(
                Settings.FONT_DIRECT_MESSAGE_COLOR)));
        directMessageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setDirectColor(ColorUtil.getHexColor(new_val));
            styleUtil.setStyles(getChatRoot(), getSettingsRoot(), applicationStyle);
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

    public void saveSettings() {
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
        settingsProperties.setProperties(settings);
    }
}
