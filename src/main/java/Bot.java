import javafx.fxml.Initializable;
import model.Rank;
import model.User;
import model.UserRepository;
import model.UserRepositoryImpl;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import util.AppProperty;

import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class Bot extends ListenerAdapter implements Initializable {

    private Properties connect;
    private UserRepository userRepository = new UserRepositoryImpl();

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String nick = event.getUser().getNick();
        updateUser(nick);
        String message = event.getMessage();
        String command = getCommandFromMessage(message);
        if (command != null) {
            runCommand(command);
        } else {
            String response = getResponseMessage(event, message);
            sendMessage(response);
        }
    }

    /**
     * The command will always be the first part of the message
     * We can split the string into parts by spaces to get each word
     * The first word if it starts with our command notifier "!" will get returned
     * Otherwise it will return null
     */
    private String getCommandFromMessage(String message) {
        String[] msgParts = message.split(" ");
        if (msgParts[0].startsWith("!")) {
            return msgParts[0];
        } else {
            return null;
        }
    }

    private void runCommand(String command) {
        System.out.println("This was command: " + command);
    }

    private String getResponseMessage(GenericMessageEvent event, String message) {
        System.out.println("This is message: " + message + " from user: " + event.getUser().getNick());
        return "";
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        Main.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(String message) {
        Main.bot.sendIRC().message("#" + connect.getProperty("twitch.channel"), message);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connect = AppProperty.getProperty("connect.properties");
    }

    private void updateUser(String nick) {
        User userByName = userRepository.getUserByName(nick);
        if (userByName == null) {
            createNewUser(nick);
        } else {
            updateExistingUser(userByName);
        }
    }

    private void updateExistingUser(User userByName) {
        User user = new User();
        Rank currentRank = getCurrentRank(userByName);
        int exp = userByName.getExp() + 1;
        int rankExp = currentRank.getExp();
        if (rankExp <= exp) {
            user.setLevel(userByName.getLevel() + 1);
            System.out.println("new");
        } else {
            user.setLevel(userByName.getLevel());
        }
        user.setName(userByName.getName());
        user.setFirstMessage(userByName.getFirstMessage());
        user.setLastMessage(new Date());
        user.setExp(exp);
        userRepository.update(user);
    }

    private void createNewUser(String nick) {
        User user = new User();
        user.setName(nick);
        user.setFirstMessage(new Date());
        user.setLastMessage(new Date());
        user.setLevel(0);
        user.setExp(1);
        userRepository.add(user);
    }

    private Rank getCurrentRank(User userByName) {
        return Rank.values()[userByName.getLevel()];
    }
}
