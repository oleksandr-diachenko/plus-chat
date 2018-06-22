package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexander Diachenko.
 */
public class TimeUtil {

    /**
     * Convert Date to String
     * @param date
     * @return String in dd-MM-yyyy HH:mm format
     */
    public static String getDateToString(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }
}
