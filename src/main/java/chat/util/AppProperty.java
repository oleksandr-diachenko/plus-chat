package chat.util;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class AppProperty{

    private String path;

    public AppProperty(String path) {
        this.path = path;
    }

    public Properties loadProperty() {
        Properties properties = new Properties();
        try (FileInputStream file = new FileInputStream(path)) {
            properties.load(file);
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Properties " + path + " failed to load. \n" +
                    "Put properties to settings/ and restart application.", exception);
        }
        return properties;
    }

    public void storeProperties(Properties properties) {
        try (OutputStream output = new FileOutputStream(path)) {
            properties.store(output, null);
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Properties " + path + " failed to save. \n" +
                    "Put properties to settings/", exception);
        }
    }
}
