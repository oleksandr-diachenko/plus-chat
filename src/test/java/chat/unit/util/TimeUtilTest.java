package chat.unit.util;

import chat.util.TimeUtil;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko
 */
public class TimeUtilTest {

    @Test
    public void dateTo_dd_MM_yyyy_HH_mm() {
        final Date date = new Date(100);
        final String dateToString = TimeUtil.getDateToString(date);

        assertEquals("01-01-1970 02:00", dateToString);
    }
}
