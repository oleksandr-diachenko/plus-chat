package twitch;

import chat.util.JSONParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class JSONParserTest {

    @Test
    public void readJsonFileTest() {
        String jsonString = JSONParser.readFile(getResource("/json/simpleJsonOneObject.json"));
        assertEquals("{  \"name\": \"Alex\",  \"nick\": \"POSITIV\",  \"birthday\": 1989}", jsonString);
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
