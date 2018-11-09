package chat.component;

import chat.util.AppProperty;
import chat.util.Settings;
import chat.util.StyleUtil;
import javafx.scene.Node;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public SettingsDialog(@Qualifier("settingsProperties") final AppProperty settingsProperties,
                          final StyleUtil styleUtil) {
        this.settingsProperties = settingsProperties;
        this.styleUtil = styleUtil;
    }

    @Override
    protected void setStageSettings(final Stage stage) {
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
    }

    @Override
    protected void initOwner(final Stage owner, final Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setEvents(final Stage stage) {
        Properties settings = this.settingsProperties.getProperty();
        stage.setOnShown(event -> {
            final Set<Node> labels = stage.getScene().getRoot().lookupAll(".label");
            labels.forEach(label ->
                    label.setStyle(this.styleUtil.getLabelStyle(settings.getProperty(Settings.FONT_NICK_COLOR))));
        });

        stage.setOnCloseRequest(event -> {
            final Node setting = stage.getOwner().getScene().getRoot().lookup("#setting");
            Node chatRoot = stage.getOwner().getScene().getRoot().lookup("#root");
            setting.setDisable(false);
            this.styleUtil.reverseStyle(settings, (Stage) stage.getOwner(), chatRoot, getRoot());
        });
    }

    @Override
    protected String getFXMLName() {
        return "settings";
    }

    @Override
    protected String getCSSName() {
        return this.paths.getSettingsCSS();
    }

    @Override
    protected String getTitleName() {
        return "";
    }
}
