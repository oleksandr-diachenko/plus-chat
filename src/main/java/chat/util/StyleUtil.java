package chat.util;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Alexander Diachenko
 */
public class StyleUtil {

    public static String getRootStyle(final String baseColor, final String backgroundColor) {
        return "-fx-base: " + baseColor + ";" +
                "-fx-background: " + backgroundColor + ";";
    }

    public static String getLabelStyle(final String color) {
        return "-fx-text-fill: " + color + ";";
    }

    public static String getTextStyle(final String fontSize, final String color) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + color + ";";
    }

    public static void setMessageStyle(final Node chatRoot, final String fontSize, final String nickColor,
                                       final String separatorColor, final String messageColor, final String directMessageColor) {
        final Set<Node> names = chatRoot.lookupAll("#user-name");
        final Set<Node> separators = chatRoot.lookupAll("#separator");
        final Set<Node> messages = chatRoot.lookupAll("#user-message");
        final Set<Node> directMessages = chatRoot.lookupAll("#user-direct-message");
        names.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, nickColor)));
        separators.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, separatorColor)));
        messages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, messageColor)));
        directMessages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, directMessageColor)));
    }

    public static void setLabelStyle(final Node root, final String color) {
        final Set<Node> labels = root.lookupAll(".label");
        labels.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getLabelStyle(color)));
    }

    public static void setRootStyle(final List<Node> roots, final String baseColor, final String backgroundColor) {
        for (Node root : roots) {
            root.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        }
    }

    public static void reverseStyle(final Properties settings, final Stage owner, final Node chatRoot) {
        StyleUtil.setMessageStyle(chatRoot,
                settings.getProperty(Settings.FONT_SIZE),
                settings.getProperty(Settings.NICK_FONT_COLOR),
                settings.getProperty(Settings.SEPARATOR_FONT_COLOR),
                settings.getProperty(Settings.MESSAGE_FONT_COLOR),
                settings.getProperty(Settings.DIRECT_MESSAGE_FONT_COLOR)
        );
        StyleUtil.setRootStyle(Collections.singletonList(chatRoot), settings.getProperty(Settings.ROOT_BASE_COLOR),
                settings.getProperty(Settings.ROOT_BACKGROUND_COLOR));
        owner.setOpacity(Double.parseDouble(settings.getProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY)) / 100);
    }
}
