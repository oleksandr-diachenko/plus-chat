package chat.bot;

import chat.sevice.Bot;
import chat.util.AppProperty;
import chat.util.Paths;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Log4j2
@Getter
@Setter
@Component
public class TwitchBotStarter implements Startable {

    public static PircBotX bot;
    @Autowired
    private AppProperty twitchProperties;
    @Autowired
    private Bot listener;
    @Autowired
    private Paths paths;

    public void start() {
        Thread thread = new Thread(() -> {
            Properties connect = twitchProperties.loadProperty();
            Configuration config = new Configuration.Builder()
                    .setName(connect.getProperty("botname"))
                    .addServer("irc.chat.twitch.tv", 6667)
                    .setServerPassword(connect.getProperty("oauth"))
                    .setAutoReconnect(true)
                    .addListener(listener)
                    .addAutoJoinChannel("#" + connect.getProperty("channel"))
                    .buildConfiguration();
            bot = new PircBotX(config);
            try {
                bot.startBot();
            } catch (IOException | IrcException exception) {
                log.error(exception.getMessage(), exception);
                throw new RuntimeException("Bot failed to start.\n " +
                        "Check properties in " + paths.getTwitchProperties() + " " +
                        "and restart application.", exception);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public Bot getListener() {
        return listener;
    }
}
