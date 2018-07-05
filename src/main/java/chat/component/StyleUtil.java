package chat.component;

import java.util.Properties;

/**
 * @author Alexander Diachenko
 */
public class StyleUtil {

    public static String getRootStyle(Properties settings) {
        return "-fx-base: " + settings.getProperty("root.base.color") + ";" +
                "-fx-background: " + settings.getProperty("root.background.color") + ";";
    }

    public static String getLabelStyle(String nickColor) {
        return "-fx-text-fill: " + nickColor + ";";
    }

    public static String getTextStyle(String fontSize, String nickColor) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + nickColor + ";";
    }
}
