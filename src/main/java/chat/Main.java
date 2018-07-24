package chat;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import chat.util.Settings;
import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        stage = primaryStage;
        primaryStage.setAlwaysOnTop(Boolean.parseBoolean(settings.getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        final String language = settings.getProperty(Settings.ROOT_LANGUAGE);
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        try {
            final Region root = getRoot(bundle);
            final UndecoratorScene undecorator = getScene(primaryStage, settings, root);
            stageEvents(primaryStage, undecorator);
            primaryStage.setScene(undecorator);
            primaryStage.setTitle("(+) chat");
            primaryStage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    private UndecoratorScene getScene(final Stage primaryStage, final Properties settings, final Region root) {
        final UndecoratorScene undecorator = new UndecoratorScene(primaryStage, root);
        undecorator.setFadeInTransition();
        undecorator.setBackgroundOpacity(0);
        undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/chat.css");
        return undecorator;
    }

    private void stageEvents(final Stage primaryStage, final UndecoratorScene undecorator) {
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
    }

    private Region getRoot(final ResourceBundle bundle) throws IOException {
        return FXMLLoader.load(getClass().getResource("/view/chat.fxml"), bundle);
    }
}
