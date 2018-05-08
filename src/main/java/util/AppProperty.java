package util;

import model.repository.RankRepositoryImpl;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class AppProperty {

    private final static Logger logger = Logger.getLogger(AppProperty.class);

    public static Properties getProperty(String fileName) {
        Properties mainProperties = new Properties();
        String path = "./settings/" + fileName;
        try {
            FileInputStream file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return mainProperties;
    }
}
