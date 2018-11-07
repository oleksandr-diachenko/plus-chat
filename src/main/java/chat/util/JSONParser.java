package chat.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author Alexander Diachenko
 */
public class JSONParser {

    private final static Logger logger = LogManager.getLogger(JSONParser.class);

    public static String readFile(final String fileName) {
        String result = "";
        try (final BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return result;
    }
}
