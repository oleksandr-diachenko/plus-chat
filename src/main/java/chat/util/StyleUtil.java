package chat.util;

import chat.controller.ApplicationStyle;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Alexander Diachenko
 */
@Component
@NoArgsConstructor
public class StyleUtil {

    private ApplicationStyle applicationStyle;

    @Autowired
    public StyleUtil(final ApplicationStyle applicationStyle) {
        this.applicationStyle = applicationStyle;
    }

    public String getRootStyle(final String baseColor, final String backgroundColor) {
        return "-fx-base: " + baseColor + ";" +
                "-fx-background: " + backgroundColor + ";";
    }

    public String getLabelStyle(final String color) {
        return "-fx-text-fill: " + color + ";";
    }

    public String getTextStyle(final String fontSize, final String color) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + color + ";";
    }

    public void setStyles(final Node chatRoot, final Node settingRoot, final ApplicationStyle applicationStyle) {
        chatRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() +
                "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        settingRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() +
                "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        final Set<Node> names = chatRoot.lookupAll("#user-name");
        final Set<Node> separators = chatRoot.lookupAll("#separator");
        final Set<Node> messages = chatRoot.lookupAll("#user-message");
        final Set<Node> directMessages = chatRoot.lookupAll("#user-direct-message");
        final String fontSize = applicationStyle.getFontSize();
        names.iterator().forEachRemaining(node -> {
            node.setStyle(this.getTextStyle(fontSize, applicationStyle.getNickColor()));
        });
        separators.iterator().forEachRemaining(node ->
                node.setStyle(this.getTextStyle(fontSize, applicationStyle.getSeparatorColor())));
        messages.iterator().forEachRemaining(node ->
                node.setStyle(this.getTextStyle(fontSize, applicationStyle.getMessageColor())));
        directMessages.iterator().forEachRemaining(node ->
                node.setStyle(this.getTextStyle(fontSize, applicationStyle.getDirectColor())));
    }

    public void setLabelStyle(final Node settingRoot, final String color) {
        final Set<Node> labels = settingRoot.lookupAll(".label");
        labels.iterator().forEachRemaining(node -> node.setStyle(this.getLabelStyle(color)));
    }

    public void setRootStyle(final List<Node> roots, final String baseColor,
                                    final String backgroundColor) {
        roots.forEach(root -> root.setStyle("-fx-base: " + baseColor +
                "; -fx-background: " + backgroundColor + ";"));
    }

    public void reverseStyle(final Properties settings, final Stage owner,
                                    final Node chatRoot, final Node settingRoot) {
        applicationStyle.reverse();
        this.setStyles(chatRoot, settingRoot, applicationStyle);
        owner.setOpacity(Double.parseDouble(
                settings.getProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY)) / 100);
    }
}
