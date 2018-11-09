package chat.component;

import chat.controller.SpringStageLoader;
import chat.util.AppProperty;
import chat.util.Paths;
import chat.util.Settings;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ChatDialog {

    private final static Logger logger = LogManager.getLogger(ChatDialog.class);

    public static Stage chatStage;
    private AppProperty settingsProperties;
    private SpringStageLoader springStageLoader;
    private Paths paths;

    public ChatDialog() {
        //do nothing
    }

    @Autowired
    public ChatDialog(@Qualifier("settingsProperties") final AppProperty settingsProperties,
                      final SpringStageLoader springStageLoader, final Paths paths) {
        this.settingsProperties = settingsProperties;
        this.springStageLoader = springStageLoader;
        this.paths = paths;
    }

    public void openDialog() {
        final Stage stage = new Stage();
        try {
            final UndecoratorScene undecorator = getScene(stage);

            additionalStaffs(stage, undecorator);

            undecorator.getStylesheets().add(this.paths.getChatCSS());
            stage.setScene(undecorator);
            stage.setTitle("(+) chat");
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Chat view failed to load");
        }
    }

    private void additionalStaffs(Stage primaryStage, UndecoratorScene undecorator) {
        chatStage = primaryStage;
        chatStage.getIcons().add(new Image(this.paths.getLogo()));
        setAlwaysOnTop(primaryStage);
        stageEvents(primaryStage, undecorator);
    }

    private void setAlwaysOnTop(Stage primaryStage) {
        primaryStage.setAlwaysOnTop(Boolean.parseBoolean(getSettings().getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
    }

    private Properties getSettings() {
        return this.settingsProperties.getProperty();
    }

    private UndecoratorScene getScene(final Stage stage) throws IOException {
        return new UndecoratorScene(stage, getRoot());
    }

    private Region getRoot() throws IOException {
        return this.springStageLoader.load("chat");
    }

    private static void stageEvents(final Stage primaryStage, final UndecoratorScene undecorator) {
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
    }
}
