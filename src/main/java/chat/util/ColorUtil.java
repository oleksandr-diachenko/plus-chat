package chat.util;

import javafx.scene.paint.Color;

/**
 * @author Alexander Diachenko
 */
public class ColorUtil {

    public static String getHexColor(final Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
