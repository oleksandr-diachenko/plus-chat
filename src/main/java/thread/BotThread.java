package thread;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import util.AppProperty;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class BotThread implements Runnable {

    public static PircBotX bot;

    @Override
    public void run() {
        Properties connect = AppProperty.getProperty("connect.properties");
        Configuration config = new Configuration.Builder()
                .setName(connect.getProperty("twitch.botname"))
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword(connect.getProperty("twitch.oauth"))
                .addListener(new Bot())
                .addAutoJoinChannel("#" + connect.getProperty("twitch.channel"))
                .buildConfiguration();


        bot = new PircBotX(config);
        try {
            bot.startBot();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }
}
