import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.pircbotx.PircBotX;

public class Main extends Application {

	public static PircBotX bot;
	public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(Main.class.getResource("/view/chat.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat");
        primaryStage.show();
//        Properties connect = AppProperty.getProperty("connect.properties");
//        Configuration config = new Configuration.Builder()
//                .setName(connect.getProperty("twitch.botname"))
//                .addServer("irc.chat.twitch.tv", 6667)
//                .setServerPassword(connect.getProperty("twitch.oauth"))
//                .addListener(new Bot())
//                .addAutoJoinChannel("#" + connect.getProperty("twitch.channel"))
//                .buildConfiguration();
//
//        bot = new PircBotX(config);
//        bot.startBot();
    }
}