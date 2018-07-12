package chat.util;

import javafx.scene.Node;

import java.util.*;

/**
 * @author Alexander Diachenko
 */
public class StyleUtil {

    public static String getRootStyle(final String baseColor, final String backgroundColor) {
        return "-fx-base: " + baseColor + ";" +
                "-fx-background: " + backgroundColor + ";";
    }

    public static String getLabelStyle(final String nickColor) {
        return "-fx-text-fill: " + nickColor + ";";
    }

    public static String getTextStyle(final String fontSize, final String nickColor) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + nickColor + ";";
    }

    public static void setLabelStyle(final Node chatRoot, final Node settingRoot, final String fontSize, final String nickColor, final String separatorColor, final String messageColor) {
        final Set<Node> names = chatRoot.lookupAll("#user-name");
        final Set<Node> separators = chatRoot.lookupAll("#separator");
        final Set<Node> messages = chatRoot.lookupAll("#user-message");
        names.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, nickColor)));
        separators.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, separatorColor)));
        messages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, messageColor)));

        if (settingRoot != null) {
            final Set<Node> labels = settingRoot.lookupAll(".label");
            labels.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getLabelStyle(nickColor)));
        }
    }

    public static void setRootStyle(final List<Node> roots, final String baseColor, final String backgroundColor) {
        for (Node root : roots) {
            root.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        }
    }

    public static void reverseStyle(final Properties settings, final Node chatRoot) {
        StyleUtil.setLabelStyle(chatRoot, null,
                settings.getProperty("font.size"),
                settings.getProperty("nick.font.color"),
                settings.getProperty("separator.font.color"),
                settings.getProperty("message.font.color")
        );
        StyleUtil.setRootStyle(Collections.singletonList(chatRoot), settings.getProperty("root.base.color"), settings.getProperty("root.background.color"));
    }
}
