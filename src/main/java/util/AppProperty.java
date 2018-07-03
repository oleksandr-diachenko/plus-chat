package util;

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

    public static Properties getProperty(String path) {
        Properties mainProperties = new Properties();
        try {
            FileInputStream file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return mainProperties;
    }

    public static Properties setProperties(Properties properties) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(".settings/settings.properties");
            properties.store(output, null);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return properties;
    }
}
