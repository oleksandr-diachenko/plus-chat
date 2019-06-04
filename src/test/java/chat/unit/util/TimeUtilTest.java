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
    public void shouldReturnFormattedDate() {
        LocalDateTime dateTime = LocalDateTime.of(1989, 5, 23, 13, 45);
        String formattedDate = TimeUtil.formatDate(dateTime);
        assertEquals("1989-05-23 13:45:00", formattedDate);
    }
}
