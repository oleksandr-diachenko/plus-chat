package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class AppProperty {

    public static Properties getProperty(String fileName) {
        Properties mainProperties = new Properties();
        String path = "./settings/" + fileName;
        try {
            FileInputStream file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mainProperties;
    }
}
