package chat.component;

import chat.util.AppProperty;
import chat.util.Settings;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ChatDialog extends AbstractDialog {

    private final static Logger logger = LogManager.getLogger(ChatDialog.class);

    private AppProperty settingsProperties;

    public ChatDialog() {
        //do nothing
    }

    @Autowired
    public ChatDialog(@Qualifier("settingsProperties") final AppProperty settingsProperties) {
        this.settingsProperties = settingsProperties;
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        //do nothing
    }

    @Override
    protected void setEvents(Stage stage) {
        stage.getIcons().add(new Image(this.paths.getLogo()));
        setAlwaysOnTop(stage);
        stage.setOnCloseRequest(we -> {
            we.consume();
            getScene().setFadeOutTransition();
        });
    }

    private void setAlwaysOnTop(Stage primaryStage) {
        primaryStage.setAlwaysOnTop(Boolean.parseBoolean(getSettings().getProperty(Settings.ROOT_ALWAYS_ON_TOP)));
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
