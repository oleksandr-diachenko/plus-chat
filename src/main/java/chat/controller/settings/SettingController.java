package chat.controller.settings;

import chat.component.ConfirmDialog;
import chat.controller.ChatController;
import chat.controller.ConfirmController;
import chat.controller.Customizable;
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

import java.util.List;
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
    private SettingDataTabController settingDataTabController;
    private SettingColorTabController settingColorTabController;
    private SettingFontTabController settingFontTabController;
    private ChatController chatController;
    private ConfirmController confirmController;
    private List<Customizable> controllers;

    @Autowired
    public SettingController(AppProperty settingsProperties, StyleUtil styleUtil,
                             ConfirmDialog confirmDialog, SettingColorTabController settingColorTabController,
                             SettingFontTabController settingFontTabController, ChatController chatController,
                             SettingDataTabController settingDataTabController, ConfirmController confirmController, List<Customizable> controllers) {
        this.settingsProperties = settingsProperties;
        this.styleUtil = styleUtil;
        this.confirmDialog = confirmDialog;
        this.settingDataTabController = settingDataTabController;
        this.settingColorTabController = settingColorTabController;
        this.settingFontTabController = settingFontTabController;
        this.chatController = chatController;
        this.confirmController = confirmController;
        this.controllers = controllers;
    }

    @FXML
    public void initialize() {
        settingColorTabController.setRoot(root);
        settingFontTabController.setRoot(root);
        settings = settingsProperties.loadProperty();
        root.setStyle(styleUtil.getRootStyle(settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
    }

    public void reloadAction() {
        settingDataTabController.reload();
    }

    public void confirmAction() {
        confirmDialog.openDialog(getStage());
        Stage stage = confirmDialog.getStage();
        stage.setOnCloseRequest(event -> {
            if (confirmController.isConfirmed()) {
                flushSettings(settings);
                chatController.setSettings(settings);
                chatController.enableSettingsButton();
            }
        });
    }

    private void flushSettings(Properties settings) {
        for (Customizable controller : controllers) {
            controller.customize(settings);
        }
    }

    public void cancelAction() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
