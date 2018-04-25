import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Main {

	public static final String BOTNAME = "POSITIV_BOT";
	public static final String OAUTH = "oauth:1v5qrmxepzzc2q8kgb9bjgvut3uvvv";
	public static final String CHANNEL = "greyphan";

	public static PircBotX bot;

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration.Builder()
				.setName(BOTNAME)
				.addServer("irc.chat.twitch.tv", 6667)
				.setServerPassword(OAUTH)
				.addListener(new Bot())
				.addAutoJoinChannel("#" + CHANNEL)
				.buildConfiguration();

		bot = new PircBotX(config);
		bot.startBot();
	}
}