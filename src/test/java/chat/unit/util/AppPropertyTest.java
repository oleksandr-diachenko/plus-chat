package chat.unit.util;

import chat.util.AppProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class AppPropertyTest {

    @Autowired
    private AppProperty simpleProperties;

    @Test
    public void readPropertiesTest() {
        final Properties properties = this.simpleProperties.getProperty();

        final String name = properties.getProperty("name");

        assertEquals("alex", name);
    }
}
