package chat.util;

import javafx.scene.paint.Color;

/**
 * @author Alexander Diachenko
 */
public class ColorUtil {

    private ColorUtil() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static String getHexColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
