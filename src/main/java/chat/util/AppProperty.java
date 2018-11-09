package chat.util;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class AppProperty{

    private final static Logger logger = LogManager.getLogger(AppProperty.class);

    private String path;

    public AppProperty(String path) {
        this.path = path;
    }

    public Properties getProperty() {
        final Properties properties = new Properties();
        try (final FileInputStream file = new FileInputStream(this.path)) {
            properties.load(file);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Properties " + this.path + " failed to load. \n" +
                    "Put properties to settings/ and restart application.");
        }
        return properties;
    }

    public Properties setProperties(final Properties properties) {
        try (final OutputStream output = new FileOutputStream(this.path)) {
            properties.store(output, null);
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Properties " + this.path + " failed to save. \n" +
                    "Put properties to settings/");
        }
        return properties;
    }
}
