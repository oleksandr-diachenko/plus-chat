package chat.util;

import chat.controller.ApplicationStyle;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.*;

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

    public static void setStyles(final ApplicationStyle applicationStyle) {
        final Node chatRoot = applicationStyle.getChatRoot();
        chatRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() + "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        final Node settingRoot = applicationStyle.getSettingRoot();
        settingRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() + "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        final Set<Node> names = chatRoot.lookupAll("#user-name");
        final Set<Node> separators = chatRoot.lookupAll("#separator");
        final Set<Node> messages = chatRoot.lookupAll("#user-message");
        final Set<Node> directMessages = chatRoot.lookupAll("#user-direct-message");
        final String fontSize = applicationStyle.getFontSize();
        names.iterator().forEachRemaining(node -> {
            node.setStyle(StyleUtil.getTextStyle(fontSize, applicationStyle.getNickColor()));
        });
        separators.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, applicationStyle.getSeparatorColor())));
        messages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, applicationStyle.getMessageColor())));
        directMessages.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getTextStyle(fontSize, applicationStyle.getDirectColor())));
    }

    public static void setLabelStyle(final Node settingRoot, final String color) {
        final Set<Node> labels = settingRoot.lookupAll(".label");
        labels.iterator().forEachRemaining(node -> node.setStyle(StyleUtil.getLabelStyle(color)));
    }

    public static void setRootStyle(final List<Node> roots, final String baseColor, final String backgroundColor) {
        roots.forEach(root -> root.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";"));
    }

    public static void reverseStyle(final Properties settings, final Stage owner, final Node chatRoot, final Node settingRoot) {
        StyleUtil.setStyles(new ApplicationStyle(chatRoot, settingRoot, settings));
        owner.setOpacity(Double.parseDouble(settings.getProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY)) / 100);
    }
}
