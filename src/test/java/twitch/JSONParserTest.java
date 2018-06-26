package twitch;

import org.junit.Test;
import util.JSONParser;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class JSONParserTest {

    @Test
    public void jsonToStringTest() {
        String jsonString = JSONParser.readFile(getResource());
        assertEquals("{  \"name\": \"Alex\",  \"nick\": \"POSITIV\",  \"birthday\": 1989}", jsonString);
    }

    private String getResource() {
        return getClass().getResource("/json/simpleJsonOneObject").getPath();
    }
}
