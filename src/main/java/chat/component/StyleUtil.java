package chat.component;

import javafx.scene.Node;

import java.util.Properties;
import java.util.Set;

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

    public static void setLabelStyle(Node chatRoot, Node settingRoot, String fontSize, String nickColor, String separatorColor, String messageColor) {
        Set<Node> names = chatRoot.lookupAll("#user-name");
        Set<Node> separators = chatRoot.lookupAll("#separator");
        Set<Node> messages = chatRoot.lookupAll("#user-message");
        names.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, nickColor)));
        separators.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, separatorColor)));
        messages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, messageColor)));

        if(settingRoot != null) {
            Set<Node> labels = settingRoot.lookupAll(".label");
            labels.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getLabelStyle(nickColor)));
        }
    }

    public static void setRootStyle(Node chatRoot, Node settingRoot, String baseColor, String backgroundColor) {
        chatRoot.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        if (settingRoot != null) {
            settingRoot.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        }
    }

    public static void reverseStyle(Properties settings, Node chatRoot) {
        StyleUtil.setLabelStyle(chatRoot, null,
                settings.getProperty("font.size"),
                settings.getProperty("nick.font.color"),
                settings.getProperty("separator.font.color"),
                settings.getProperty("message.font.color")
        );
        StyleUtil.setRootStyle(chatRoot, null, settings.getProperty("root.base.color"), settings.getProperty("root.background.color"));
    }
}
