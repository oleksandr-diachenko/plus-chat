package chat.unit.util;

import chat.util.JSONParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class JSONParserTest {

    @Test
    public void readJsonFileTest() {
        final String jsonString = JSONParser.readFile(getResource("/json/simpleJsonOneObject.json"));
        assertEquals("{  \"name\": \"Alex\",  \"nick\": \"POSITIV\",  \"birthday\": 1989}", jsonString);
    }

    private String getResource(final String path) {
        return getClass().getResource(path).getPath();
    }
}
