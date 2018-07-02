import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setAlwaysOnTop(true);
        primaryStage.getIcons().add(new Image("/img/logo.png"));
        Region root = FXMLLoader.load(getClass().getResource("/view/chat.fxml"));
        UndecoratorScene undecorator = new UndecoratorScene(primaryStage, root);
        undecorator.setFadeInTransition();
        undecorator.setBackgroundOpacity(0);
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
        primaryStage.setScene(undecorator);
        primaryStage.setTitle("Chat");
        primaryStage.show();
    }
}