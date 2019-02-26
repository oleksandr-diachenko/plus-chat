package chat.util;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author Alexander Diachenko
 */
@Log4j2
public class JSONParser {

    public static String readFile(String fileName) {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
        return result;
    }
}
