package util;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * @author Alexander Diachenko.
 */
public class StringUtil {

    private final static Logger logger = Logger.getLogger(StringUtil.class);

    /**
     * Convert string to byte array and return string in UTF-8
     * @param string
     * @return string in UTF-8
     */
    public static String getUTF8String(String string) {
        byte bytes[] = string.getBytes();
        try {
           return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return "";
    }
}
