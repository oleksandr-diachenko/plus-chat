package chat.component;

import chat.controller.SpringStageLoader;
import chat.util.AppProperty;
import chat.util.Settings;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class ChatDialog {

    private final static Logger logger = LogManager.getLogger(ChatDialog.class);

    public static Stage chatStage;

    public void openDialog() {
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        final Stage primaryStage = new Stage();
        chatStage = primaryStage;
        primaryStage.setAlwaysOnTop(Boolean.parseBoolean(settings.getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        try {
            final UndecoratorScene undecorator = new UndecoratorScene(primaryStage, SpringStageLoader.load("chat"));
            stageEvents(primaryStage, undecorator);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/chat.css");
            primaryStage.setScene(undecorator);
            primaryStage.setTitle("(+) chat");
            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Chat view failed to load");
        }
    }

    private static void stageEvents(final Stage primaryStage, final UndecoratorScene undecorator) {
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
    }
}
