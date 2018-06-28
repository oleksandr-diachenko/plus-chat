import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Region root = FXMLLoader.load(getClass().getResource("/view/chat.fxml"));
        root.setStyle("-fx-background: #212121; -fx-font-family: \"Arial\";");
        UndecoratorScene undecorator = new UndecoratorScene(primaryStage, root);
        undecorator.setFadeInTransition();
        primaryStage.setOnCloseRequest(we -> {
            we.consume();
            undecorator.setFadeOutTransition();
        });
        primaryStage.setScene(undecorator);
        primaryStage.setTitle("Chat");
        primaryStage.show();
    }
}