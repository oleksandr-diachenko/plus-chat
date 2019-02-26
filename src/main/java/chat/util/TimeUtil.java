package chat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Alexander Diachenko.
 */
public class TimeUtil {

    /**
     * Convert Date to String
     *
     * @param date
     * @return String in dd-MM-yyyy HH:mm:ss format
     */
    public static String getDateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
