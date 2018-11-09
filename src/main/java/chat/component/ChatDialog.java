package chat.component;

import chat.util.AppProperty;
import chat.util.Settings;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public ChatDialog(@Qualifier("settingsProperties") final AppProperty settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    @Override
    protected void setStageSettings(final Stage stage) {
        stage.getIcons().add(new Image(this.paths.getLogo()));
        setAlwaysOnTop(stage);
    }

    @Override
    protected void initOwner(final Stage owner, final Stage stage) {
        //do nothing
    }

    @Override
    protected void setEvents(final Stage stage) {
        stage.setOnCloseRequest(we -> {
            we.consume();
            getScene().setFadeOutTransition();
        });
    }

    private void setAlwaysOnTop(final Stage stage) {
        stage.setAlwaysOnTop(Boolean.parseBoolean(getSettings().getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
    }

    private Properties getSettings() {
        return this.settingsProperties.getProperty();
    }

    @Override
    protected String getFXMLName() {
        return "chat";
    }

    @Override
    protected String getCSSName() {
        return this.paths.getChatCSS();
    }

    @Override
    protected String getTitleName() {
        return "(+) chat";
    }
}
