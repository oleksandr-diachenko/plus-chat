package chat;

import chat.controller.SpringStageLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Alexander Diachenko
 */
public class PlusChatFX extends Application {

    private static ClassPathXmlApplicationContext context;
    public static Stage stage;

    @Override
    public void init() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        SpringStageLoader.loadMain().show();
    }

    @Override
    public void stop() {
        context.close();
    }
}
