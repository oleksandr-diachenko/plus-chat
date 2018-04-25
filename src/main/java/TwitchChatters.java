import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Alexander Diachenko.
 */
public class TwitchChatters {

    private String channel;

    public TwitchChatters(String channel) {
        this.channel = channel;
    }

    public int getChattersCount() {
        return getData().getInt("chatter_count");
    }

    public JSONArray getViewers() {
        return getData().getJSONObject("chatters").getJSONArray("viewers");
    }

    public JSONArray getModerators() {
        return getData().getJSONObject("chatters").getJSONArray("moderators");
    }

    public JSONArray getAdministrators() {
        return getData().getJSONObject("chatters").getJSONArray("admins");
    }

    public JSONArray getStaffs() {
        return getData().getJSONObject("chatters").getJSONArray("staff");
    }

    private JSONObject getData() {
        try {
            return new JSONObject(JSONParser.readUrl("https://tmi.twitch.tv/group/user/" + channel + "/chatters"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
