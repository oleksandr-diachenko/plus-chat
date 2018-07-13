package twitch;

import org.junit.Test;
import chat.util.AppProperty;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class AppPropertyTest {

    @Test
    public void readPropertiesTest() {
        Properties properties = AppProperty.getProperty(getResource("/property/simplePropertyOneObject.properties"));

        String name = properties.getProperty("name");

        assertEquals("alex", name);
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
