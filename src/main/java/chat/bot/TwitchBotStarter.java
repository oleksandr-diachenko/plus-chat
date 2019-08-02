package chat.bot;

import chat.util.AppProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Log4j2
@Data
@Component
public class TwitchBotStarter extends AbstractBotStarter {

    @Autowired
    private AppProperty twitchProperties;

    @Override
    protected void configureBot() {
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
    }

    @Override
    protected String getBotProperties() {
        return paths.getTwitchProperties();
    }
}
