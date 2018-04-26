import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import util.AppProperty;

import java.util.Properties;

public class Main {

	public static PircBotX bot;

	public static void main(String[] args) throws Exception {
		Properties connect = AppProperty.getProperty("connect.properties");
		Configuration config = new Configuration.Builder()
				.setName(connect.getProperty("twitch.botname"))
				.addServer("irc.chat.twitch.tv", 6667)
				.setServerPassword(connect.getProperty("twitch.oauth"))
				.addListener(new Bot())
				.addAutoJoinChannel("#" + connect.getProperty("twitch.channel"))
				.buildConfiguration();

		bot = new PircBotX(config);
		bot.startBot();
	}
}