package chat.component;

import chat.util.AppProperty;
import chat.util.Settings;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class ChatDialog extends AbstractDialog {

    private AppProperty settingsProperties;

    @Autowired
    public ChatDialog(AppProperty settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    @Override
    protected String getCSSName() {
        return paths.getChatCSS();
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        //do nothing
    }

    @Override
    protected void setStageSettings(Stage stage) {
        stage.getIcons().add(new Image(paths.getLogo()));
        setAlwaysOnTop(stage);
    }

    @Override
    protected void setEvents(Stage stage) {
        stage.setOnCloseRequest(event -> {
            event.consume();
            getScene().setFadeOutTransition();
        });
    }

    @Override
    protected String getTitleName() {
        return "(+) chat";
    }

    @Override
    protected String getFXMLName() {
        return "chat";
    }

    private void setAlwaysOnTop(Stage stage) {
        boolean isOnTop = Boolean.parseBoolean(getSettings().getProperty(Settings.ROOT_ALWAYS_ON_TOP));
        stage.setAlwaysOnTop(isOnTop);
    }

    private Properties getSettings() {
        return settingsProperties.loadProperty();
    }
}
