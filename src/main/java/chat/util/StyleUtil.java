package chat.util;

import chat.controller.ApplicationStyle;
import javafx.scene.Node;
import javafx.stage.Window;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Alexander Diachenko
 */
@Component
@NoArgsConstructor
public class StyleUtil {

    private ApplicationStyle applicationStyle;

    @Autowired
    public StyleUtil(ApplicationStyle applicationStyle) {
        this.applicationStyle = applicationStyle;
    }

    public String getRootStyle(String baseColor, String backgroundColor) {
        return "-fx-base: " + baseColor + ";" +
                "-fx-background: " + backgroundColor + ";";
    }

    public String getLabelStyle(String color) {
        return "-fx-text-fill: " + color + ";";
    }

    public String getTextStyle(String fontSize, String color) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-fill: " + color + ";";
    }

    public void setStyles(Node chatRoot, Node settingRoot, ApplicationStyle applicationStyle) {
        chatRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() +
                "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        settingRoot.setStyle("-fx-base: " + applicationStyle.getBaseColor() +
                "; -fx-background: " + applicationStyle.getBackgroundColor()+ ";");
        Set<Node> names = chatRoot.lookupAll("#user-name");
        Set<Node> separators = chatRoot.lookupAll("#separator");
        Set<Node> messages = chatRoot.lookupAll("#user-message");
        Set<Node> directMessages = chatRoot.lookupAll("#user-direct-message");
        String fontSize = applicationStyle.getFontSize();
        names.iterator().forEachRemaining(node -> {
            node.setStyle(getTextStyle(fontSize, applicationStyle.getNickColor()));
        });
        separators.iterator().forEachRemaining(node ->
                node.setStyle(getTextStyle(fontSize, applicationStyle.getSeparatorColor())));
        messages.iterator().forEachRemaining(node ->
                node.setStyle(getTextStyle(fontSize, applicationStyle.getMessageColor())));
        directMessages.iterator().forEachRemaining(node ->
                node.setStyle(getTextStyle(fontSize, applicationStyle.getDirectColor())));
    }

    public void setLabelsStyle(Node root, String color) {
        Set<Node> labels = root.lookupAll(".label");
        labels.iterator().forEachRemaining(node -> node.setStyle(getLabelStyle(color)));
    }

    public void setRootStyle(List<Node> roots, String baseColor, String backgroundColor) {
        for (Node root : roots) {
            root.setStyle("-fx-base: " + baseColor + "; -fx-background: " + backgroundColor + ";");
        }
    }

    public void reverseStyle(Properties settings, Window owner,
                                    Node chatRoot, Node settingRoot) {
        applicationStyle.reverse();
        setStyles(chatRoot, settingRoot, applicationStyle);
        owner.setOpacity(Double.parseDouble(
                settings.getProperty(Settings.ROOT_BACKGROUND_TRANSPARENCY)) / 100);
    }
}
