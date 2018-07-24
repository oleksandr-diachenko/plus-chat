package chat.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class AppProperty {

    private final static Logger logger = LogManager.getLogger(AppProperty.class);

    public static Properties getProperty(final String path) {
        final Properties properties = new Properties();
        try (final FileInputStream file = new FileInputStream(path)) {
            properties.load(file);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return properties;
    }

    public static Properties setProperties(final String path, final Properties properties) {
        try (final OutputStream output = new FileOutputStream(path)) {
            properties.store(output, null);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return properties;
    }
}
