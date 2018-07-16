package chat;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }

    private final static Logger logger = Logger.getLogger(Main.class);

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        final String language = settings.getProperty("root.language");
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        try {
            final Region root = FXMLLoader.load(getClass().getResource("/view/chat.fxml"), bundle);
            final UndecoratorScene undecorator = new UndecoratorScene(primaryStage, root);
            undecorator.setFadeInTransition();
            undecorator.setBackgroundOpacity(0);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/chat.css");
            primaryStage.setOnCloseRequest(we -> {
                we.consume();
                undecorator.setFadeOutTransition();
            });
            primaryStage.setScene(undecorator);
            primaryStage.setTitle("(+) chat");
            primaryStage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }
}
