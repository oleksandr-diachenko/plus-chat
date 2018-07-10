package chat.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class AppProperty {

    private final static Logger logger = Logger.getLogger(AppProperty.class);

    public static Properties getProperty(final String path) {
        final Properties properties = new Properties();
        try (final FileInputStream file = new FileInputStream(path)) {
            properties.load(file);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return properties;
    }

    public static Properties setProperties(final Properties properties) {
        try (final OutputStream output = new FileOutputStream("./settings/settings.properties")) {
            properties.store(output, null);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return properties;
    }
}
