package chat.component;

import chat.controller.ChatController;
import chat.controller.SpringStageLoader;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class SettingsDialog {

    private final static Logger logger = LogManager.getLogger(SettingsDialog.class);

    public void openDialog(final Stage owner, final Node ownerRoot) {
        final Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        try {
            final Region root = SpringStageLoader.load("settings");
            final UndecoratorScene undecorator = getScene(stage, settings, root);
            stageEvents(owner, ownerRoot, stage, settings, root);
            root.setStyle(StyleUtil.getRootStyle(settings.getProperty(Settings.ROOT_BASE_COLOR), settings.getProperty(Settings.ROOT_BACKGROUND_COLOR)));
            stage.setScene(undecorator);
            stage.initOwner(owner);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Settings view failed to load");
        }
    }

    private UndecoratorScene getScene(final Stage stage, final Properties settings, final Region root) {
        final UndecoratorScene undecorator = new UndecoratorScene(stage, root);
        undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/settings.css");
        return undecorator;
    }

    private void stageEvents(final Stage owner, final Node ownerRoot, final Stage stage, final Properties settings, final Region root) {
        stage.setOnShown(event -> {
            final Set<Node> labels = root.lookupAll(".label");
            labels.forEach(label -> label.setStyle(StyleUtil.getLabelStyle(settings.getProperty(Settings.FONT_NICK_COLOR))));
        });

        stage.setOnCloseRequest(event -> {
            final ChatController chatController = (ChatController) ownerRoot.getUserData();
            chatController.getSetting().setDisable(false);
            StyleUtil.reverseStyle(settings, owner, ownerRoot, root);
        });
    }

}
