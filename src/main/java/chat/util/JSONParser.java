package chat.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;

public class JSONParser {

    private final static Logger logger = Logger.getLogger(JSONParser.class);

    public static String readFile(String fileName) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
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
