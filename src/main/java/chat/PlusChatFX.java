package chat;

import chat.component.ChatDialog;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Diachenko
 */
@Component
@NoArgsConstructor
public class PlusChatFX extends Application {

    private static ClassPathXmlApplicationContext context;
    private static ChatDialog chatDialog;
    private static String appContextLocation;

    @Autowired
    public PlusChatFX(ChatDialog chatDialog) {
        PlusChatFX.chatDialog = chatDialog;
    }

    @Override
    public void init() {
        context = new ClassPathXmlApplicationContext(appContextLocation);
    }

    public static void main(String[] args, String appContextLocation) {
        PlusChatFX.appContextLocation = appContextLocation;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        PlusChatFX.chatDialog.openDialog(primaryStage);
    }

    @Override
    public void stop() {
        context.close();
    }
}
