package chat.util;

import chat.TestAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Alexander Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@TestPropertySource("classpath:application.properties")
public class AppPropertyTest {

    @Autowired
    private AppProperty simpleProperty;

    @Test
    public void shouldReturnNameWhenPropertyCorrect() {
        Properties properties = simpleProperty.loadProperty();
        String name = properties.getProperty("name");
        assertEquals("alex", name);
    }

    @Test
    public void shouldReturnNullWhenPropertyIncorrect() {
        Properties properties = simpleProperty.loadProperty();
        String name = properties.getProperty("qwe");
        assertNull(name);
    }
}
