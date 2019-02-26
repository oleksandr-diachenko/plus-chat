package chat.controller.settings;

import chat.component.ConfirmDialog;
import chat.controller.ChatController;
import chat.controller.ConfirmController;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Controller
@NoArgsConstructor
public class SettingController {

    @FXML
    public Node root;
    private Properties settings;
    private AppProperty settingsProperties;
    private StyleUtil styleUtil;
    private ConfirmDialog confirmDialog;
    private SettingDataController settingDataController;
    private SettingColorController settingColorController;
    private SettingFontController settingFontController;
    private SettingGeneralController settingGeneralController;
    private SettingSoundController settingSoundController;
    private ChatController chatController;
    private ConfirmController confirmController;

    @Autowired
    public SettingController(AppProperty settingsProperties, StyleUtil styleUtil,
                             ConfirmDialog confirmDialog, SettingColorController settingColorController,
                             SettingFontController settingFontController, SettingGeneralController settingGeneralController,
                             SettingSoundController settingSoundController, ChatController chatController,
                             SettingDataController settingDataController, ConfirmController confirmController) {
        this.settingsProperties = settingsProperties;
        this.styleUtil = styleUtil;
        this.confirmDialog = confirmDialog;
        this.settingDataController = settingDataController;
        this.settingColorController = settingColorController;
        this.settingFontController = settingFontController;
        this.settingGeneralController = settingGeneralController;
        this.settingSoundController = settingSoundController;
        this.chatController = chatController;
        this.confirmController = confirmController;
    }

    @FXML
    public void initialize() {
        settingColorController.setRoot(root);
        settingFontController.setRoot(root);
        settings = settingsProperties.getProperty();
        root.setStyle(styleUtil.getRootStyle(settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
    }

    public void reloadAction() {
        settingDataController.reload();
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
        settingColorController.saveSettings(settings);
        settingFontController.saveSettings(settings);
        settingGeneralController.saveSettings(settings);
        settingSoundController.saveSettings(settings);
        chatController.setSettings(settings);
        chatController.getSetting().setDisable(false);
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
