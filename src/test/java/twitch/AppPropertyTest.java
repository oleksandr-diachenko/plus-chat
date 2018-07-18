package twitch;

import chat.util.AppProperty;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class AppPropertyTest {

    @Test
    public void readPropertiesTest() {
        final Properties properties = AppProperty.getProperty(getResource("/property/simplePropertyOneObject.properties"));

        final String name = properties.getProperty("name");

        assertEquals("alex", name);
    }

    private String getResource(final String path) {
        return getClass().getResource(path).getPath();
    }
}
