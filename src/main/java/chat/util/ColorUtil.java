package chat.util;

import javafx.scene.paint.Color;

/**
 * @author Alexander Diachenko
 */
public class ColorUtil {

    public static String getHexColor(final Color color) {
        return "#" + Integer.toHexString(color.hashCode()).substring(0, 6).toUpperCase();
    }
}
