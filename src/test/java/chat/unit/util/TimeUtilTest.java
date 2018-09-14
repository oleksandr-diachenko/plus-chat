package chat.unit.util;

import chat.util.TimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class TimeUtilTest {

    @Test
    public void dateTo_dd_MM_yyyy_HH_mm__ss() {
        final LocalDateTime dateTime = LocalDateTime.of(1989, 5, 23, 13, 45);
        final String dateToString = TimeUtil.getDateToString(dateTime);

        assertEquals("1989-05-23 13:45:00", dateToString);
    }
}
