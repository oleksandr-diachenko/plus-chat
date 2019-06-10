package chat.component.dialog;

import chat.component.CustomStage;
import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.scene.Node;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class SettingsDialog extends AbstractDialog{

    private AppProperty settingsProperties;
    private StyleUtil styleUtil;

    @Autowired
    public SettingsDialog(AppProperty settingsProperties, StyleUtil styleUtil) {
        this.settingsProperties = settingsProperties;
        this.styleUtil = styleUtil;
    }

    @Override
    protected String getCSSName() {
        return paths.getSettingsCSS();
    }

    @Override
    protected void initOwner(CustomStage owner, CustomStage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setStageSettings(CustomStage stage) {
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
    }

    @Override
    protected void setEvents(CustomStage stage) {
        Properties settings = settingsProperties.loadProperty();
        stage.setOnShown(event -> {
            Set<Node> labels = getAllLabels(stage);
            labels.forEach(label ->
                    label.setStyle(styleUtil.getLabelStyle(settings.getProperty(Settings.FONT_NICK_COLOR))));
        });

        stage.setOnCloseRequest(event -> {
            Node setting = getOwnersNode((CustomStage) stage.getOwner(), "#setting");
            Node chatRoot = getOwnersNode((CustomStage) stage.getOwner(), "#root");
            setting.setDisable(false);
            styleUtil.reverseStyle(settings, stage.getOwner(), chatRoot, getRoot());
        });
    }

    private Node getOwnersNode(CustomStage owner, String nodeId) {
        return owner.getScene().getRoot().lookup(nodeId);
    }

    private Set<Node> getAllLabels(CustomStage stage) {
        return stage.getScene().getRoot().lookupAll(".label");
    }

    @Override
    protected String getFXMLName() {
        return "settings";
    }
}
