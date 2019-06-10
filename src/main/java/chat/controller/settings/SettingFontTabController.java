package chat.controller.settings;

import chat.component.CustomStage;
import chat.component.dialog.ChatDialog;
import chat.controller.ApplicationStyle;
import chat.controller.Customizable;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingFontTabController implements Customizable {

    @FXML
    private Label fontSize;
    @FXML
    private Slider fontSizeSlider;
    private AppProperty settingsProperties;
    private ApplicationStyle applicationStyle;
    private StyleUtil styleUtil;
    private ChatDialog chatDialog;
    private Properties settings;
    private Node root;

    @Autowired
    public SettingFontTabController(AppProperty settingsProperties, ApplicationStyle applicationStyle,
                                    StyleUtil styleUtil, ChatDialog chatDialog) {
        this.settingsProperties = settingsProperties;
        this.applicationStyle = applicationStyle;
        this.styleUtil = styleUtil;
        this.chatDialog = chatDialog;
    }

    @FXML
    public void initialize() {
        settings = settingsProperties.loadProperty();
        initFontSizeSlider();
    }

    private void initFontSizeSlider() {
        String fontSizeValue = settings.getProperty(Settings.FONT_SIZE);
        fontSize.setText(fontSizeValue);
        fontSizeSlider.setValue(Double.parseDouble(fontSizeValue));
        fontSizeSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fontSize.setText(String.valueOf(Math.round(new_val.doubleValue())));
            applicationStyle.setFontSize(String.valueOf(new_val));
            styleUtil.setStyles(getChatRoot(), root, applicationStyle);
        });
    }

    private Node getChatRoot() {
        CustomStage owner = chatDialog.getStage();
        return owner.getScene().lookup("#root");
    }

    void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public void customize(Properties properties) {
        properties.setProperty(Settings.FONT_SIZE, fontSize.getText());
        settingsProperties.storeProperties(properties);
    }
}
