package chat.component;

import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.scene.Node;
import javafx.stage.Stage;
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
    protected void initOwner(Stage owner, Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setStageSettings(Stage stage) {
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
    }

    @Override
    protected void setEvents(Stage stage) {
        Properties settings = settingsProperties.getProperty();
        stage.setOnShown(event -> {
            Set<Node> labels = stage.getScene().getRoot().lookupAll(".label");
            labels.forEach(label ->
                    label.setStyle(styleUtil.getLabelStyle(settings.getProperty(Settings.FONT_NICK_COLOR))));
        });

        stage.setOnCloseRequest(event -> {
            Node setting = stage.getOwner().getScene().getRoot().lookup("#setting");
            Node chatRoot = stage.getOwner().getScene().getRoot().lookup("#root");
            setting.setDisable(false);
            styleUtil.reverseStyle(settings, (Stage) stage.getOwner(), chatRoot, getRoot());
        });
    }

    @Override
    protected String getFXMLName() {
        return "settings";
    }
}
