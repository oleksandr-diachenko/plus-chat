import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class Bot extends ListenerAdapter {

    private TextToSpeech textToSpeech = new TextToSpeech();

    /**
     * PircBotx will return the exact message sent and not the raw line
     */
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String message = event.getMessage();
        String command = getCommandFromMessage(message);

        String response = getResponse(event, message);
        if (response != null) sendMessage(response);
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

    private String getResponse(GenericMessageEvent event, String message) {
        String user = event.getUser().getNick();
        try {
            if(user.equalsIgnoreCase("greyphan")) {
                return "noob " + user + " send message: " + message;
            } else {
                textToSpeech.speak(message);
                return user + " send message: " + message;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        Main.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    private void sendMessage(String message) {
        Main.bot.sendIRC().message("#" + Main.CHANNEL, message);
    }
}