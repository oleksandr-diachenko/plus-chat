package chat.component;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ConfirmDialog extends AbstractDialog {

    @Override
    protected void setStageSettings(final Stage stage) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
    }

    @Override
    protected void initOwner(final Stage owner, final Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setEvents(final Stage stage) {
        //do nothing
    }

    @Override
    protected String getFXMLName() {
        return "confirm";
    }

    @Override
    protected String getCSSName() {
        return this.paths.getConfirmCSS();
    }

    @Override
    protected String getTitleName() {
        return "";
    }
}
