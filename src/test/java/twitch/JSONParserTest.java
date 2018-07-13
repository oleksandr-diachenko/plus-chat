package twitch;

import org.junit.Test;
import chat.util.JSONParser;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko
 */
public class JSONParserTest {

    @Test
    public void readJsonFileTest() {
        String jsonString = JSONParser.readFile(getResource("/json/simpleJsonOneObject.json"));
        assertTrue(!jsonString.isEmpty());
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
