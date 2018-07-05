package chat;

import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import chat.util.AppProperty;
import chat.util.ResourceBundleControl;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        Properties settings = AppProperty.getProperty("./settings/settings.properties");
        String language = settings.getProperty("root.language");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        Region root = FXMLLoader.load(getClass().getResource("/view/chat.fxml"), bundle);
        UndecoratorScene undecorator = new UndecoratorScene(primaryStage, root);
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
    }
}