package chat.util;

import java.nio.charset.StandardCharsets;

/**
 * @author Alexander Diachenko.
 */
public class StringUtil {

    /**
     * Convert string to byte array and return string in UTF-8
     *
     * @param string
     * @return string in UTF-8
     */
    public static String getUTF8String(final String string) {
        final byte bytes[] = string.getBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
