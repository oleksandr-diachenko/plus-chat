import model.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import util.AppProperty;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Main {

	public static PircBotX bot;

	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<User> users = mapper.readValue(JSONParser.readFile("./users.json"), new TypeReference<List<User>>(){});
		User user = createDummyUser();
		users.add(user);
		mapper.writeValue(new FileOutputStream("./users.json"), users);
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

	private static User createDummyUser() {
		User user = new User();
		user.setName("petia");
		user.setFirstMessage(new Date());
		user.setLastMessage(new Date());
		user.setLevel(1);
		user.setExp(10);
		return user;
	}
}