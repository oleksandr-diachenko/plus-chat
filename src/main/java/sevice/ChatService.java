package sevice;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import thread.Bot;
import util.AppProperty;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class ChatService extends Service<Void> {

    public static PircBotX bot;
    private ScrollPane scrollPane;
    private List<HBox> messages;

    public ChatService(ScrollPane scrollPane, List<HBox> messages) {
        this.scrollPane = scrollPane;
        this.messages = messages;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Properties connect = AppProperty.getProperty("connect.properties");
                Configuration config = new Configuration.Builder()
                        .setName(connect.getProperty("twitch.botname"))
                        .addServer("irc.chat.twitch.tv", 6667)
                        .setServerPassword(connect.getProperty("twitch.oauth"))
                        .addListener(new Bot(scrollPane, messages))
                        .addAutoJoinChannel("#" + connect.getProperty("twitch.channel"))
                        .buildConfiguration();


                bot = new PircBotX(config);
                try {
                    bot.startBot();
                } catch (IOException | IrcException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
