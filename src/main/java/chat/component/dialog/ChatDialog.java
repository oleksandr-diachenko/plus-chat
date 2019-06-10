package chat.component.dialog;

import chat.component.CustomStage;
import chat.util.AppProperty;
import chat.util.Settings;
import javafx.scene.image.Image;
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
    protected void initOwner(CustomStage owner, CustomStage stage) {
        //do nothing
    }

    @Override
    protected void setStageSettings(CustomStage stage) {
        stage.getIcons().add(new Image(paths.getLogo()));
        setAlwaysOnTop(stage);
    }

    @Override
    protected void setEvents(CustomStage stage) {
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

    private void setAlwaysOnTop(CustomStage stage) {
        boolean isOnTop = Boolean.parseBoolean(getSettings().getProperty(Settings.ROOT_ALWAYS_ON_TOP));
        stage.setAlwaysOnTop(isOnTop);
    }

    private Properties getSettings() {
        return settingsProperties.loadProperty();
    }

    public void setOpacity(double opacity) {
        getStage().setOpacity(opacity);
    }
}
