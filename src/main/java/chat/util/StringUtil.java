package chat.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * @author Alexander Diachenko.
 */
public class StringUtil {

    private final static Logger logger = LogManager.getLogger(StringUtil.class);

    /**
     * Convert string to byte array and return string in UTF-8
     *
     * @param string
     * @return string in UTF-8
     */
    public static String getUTF8String(final String string) {
        final byte bytes[] = string.getBytes();
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return string;
    }
}
