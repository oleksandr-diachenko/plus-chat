package chat;

import chat.component.ChatDialog;
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
        final ChatDialog chatDialog = new ChatDialog();
        chatDialog.openDialog();
    }

    @Override
    public void stop() {
        context.close();
    }
}
