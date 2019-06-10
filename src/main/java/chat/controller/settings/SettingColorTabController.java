package chat.controller.settings;

import chat.component.dialog.ChatDialog;
import chat.controller.ApplicationStyle;
import chat.controller.Customizable;
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
public class SettingColorTabController implements Customizable {

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
    private AppProperty settingsProperties;
    private ChatDialog chatDialog;
    private ApplicationStyle applicationStyle;
    private StyleUtil styleUtil;
    private Properties settings;
    private Node root;

    @Autowired
    public SettingColorTabController(AppProperty settingsProperties, ChatDialog chatDialog,
                                     ApplicationStyle applicationStyle, StyleUtil styleUtil) {
        this.settingsProperties = settingsProperties;
        this.chatDialog = chatDialog;
        this.applicationStyle = applicationStyle;
        this.styleUtil = styleUtil;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.loadProperty();
        initBaseColorPicker();
        initBackGroundColorPicker();
        initNickColorPicker();
        initSeparatorColorPicker();
        initMessageColorPicker();
        initDirectMessageColorPicker();
    }

    private void initBaseColorPicker() {
        baseColorPicker.setValue(getColor(Settings.ROOT_BASE_COLOR));
        baseColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBaseColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private void initBackGroundColorPicker() {
        backgroundColorPicker.setValue(getColor(Settings.ROOT_BACKGROUND_COLOR));
        backgroundColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setBackgroundColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private void initNickColorPicker() {
        nickColorPicker.setValue(getColor(Settings.FONT_NICK_COLOR));
        nickColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setNickColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private void initSeparatorColorPicker() {
        separatorColorPicker.setValue(getColor(Settings.FONT_SEPARATOR_COLOR));
        separatorColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setSeparatorColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private void initMessageColorPicker() {
        messageColorPicker.setValue(getColor(Settings.FONT_MESSAGE_COLOR));
        messageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setMessageColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private void initDirectMessageColorPicker() {
        directMessageColorPicker.setValue(getColor(Settings.FONT_DIRECT_MESSAGE_COLOR));
        directMessageColorPicker.valueProperty().addListener((ov, old_val, new_val) -> {
            applicationStyle.setDirectColor(ColorUtil.getHexColor(new_val));
            setRootStyle();
        });
    }

    private Color getColor(String rootBaseColor) {
        return Color.valueOf(settings.getProperty(rootBaseColor));
    }

    private void setRootStyle() {
        styleUtil.setStyles(getChatRoot(), root, applicationStyle);
    }

    private Node getChatRoot() {
        Stage owner = chatDialog.getStage();
        return owner.getScene().lookup("#root");
    }

    void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public void customize(Properties properties) {
        properties.setProperty(Settings.ROOT_BASE_COLOR, ColorUtil.getHexColor(
                baseColorPicker.getValue()));
        properties.setProperty(Settings.ROOT_BACKGROUND_COLOR, ColorUtil.getHexColor(
                backgroundColorPicker.getValue()));
        properties.setProperty(Settings.FONT_NICK_COLOR, ColorUtil.getHexColor(
                nickColorPicker.getValue()));
        properties.setProperty(Settings.FONT_SEPARATOR_COLOR, ColorUtil.getHexColor(
                separatorColorPicker.getValue()));
        properties.setProperty(Settings.FONT_MESSAGE_COLOR, ColorUtil.getHexColor(
                messageColorPicker.getValue()));
        properties.setProperty(Settings.FONT_DIRECT_MESSAGE_COLOR, ColorUtil.getHexColor(
                directMessageColorPicker.getValue()));
        settingsProperties.storeProperties(properties);
    }
}
