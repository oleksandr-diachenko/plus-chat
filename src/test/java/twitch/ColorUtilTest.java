package twitch;

import chat.util.ColorUtil;
import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexander Diachenko.
 */
public class ColorUtilTest {

    @Test
    public void colorToHexBlackTest() {
        final Color black = Color.rgb(0, 0, 0);

        final String hexColor = ColorUtil.getHexColor(black);

        assertEquals("#000000", hexColor);
    }

    @Test
    public void colorToHexWhiteTest() {
        final Color black = Color.rgb(255, 255, 255);

        final String hexColor = ColorUtil.getHexColor(black);

        assertEquals("#FFFFFF", hexColor);
    }

    @Test
    public void colorToHexBlueTest() {
        final Color black = Color.rgb(0, 0, 255);

        final String hexColor = ColorUtil.getHexColor(black);

        assertEquals("#0000FF", hexColor);
    }
}
