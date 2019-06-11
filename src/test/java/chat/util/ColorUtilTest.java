package chat.util;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko.
 */
public class ColorUtilTest {

    @Test
    public void shouldReturnBlackHexWhenColorBlack() {
        Color black = Color.rgb(0, 0, 0);
        String hexColor = ColorUtil.getHexColor(black);
        assertEquals("#000000", hexColor);
    }

    @Test
    public void shouldReturnWhiteHexWhenColorWhite() {
        Color black = Color.rgb(255, 255, 255);
        String hexColor = ColorUtil.getHexColor(black);
        assertEquals("#FFFFFF", hexColor);
    }

    @Test
    public void shouldReturnBlueHexWhenColorBlue() {
        Color black = Color.rgb(0, 0, 255);
        String hexColor = ColorUtil.getHexColor(black);
        assertEquals("#0000FF", hexColor);
    }
}
