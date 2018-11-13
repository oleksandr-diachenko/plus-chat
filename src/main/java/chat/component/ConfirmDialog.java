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
    protected void setStageSettings(Stage stage) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected String getFXMLName() {
        return "confirm";
    }

    @Override
    protected String getCSSName() {
        return paths.getConfirmCSS();
    }
}
