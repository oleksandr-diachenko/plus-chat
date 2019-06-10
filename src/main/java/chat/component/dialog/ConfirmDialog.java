package chat.component.dialog;

import chat.component.CustomStage;
import javafx.stage.Modality;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ConfirmDialog extends AbstractDialog {

    @Override
    protected void initOwner(CustomStage owner, CustomStage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setStageSettings(CustomStage stage) {
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
    }

    @Override
    protected String getCSSName() {
        return paths.getConfirmCSS();
    }

    @Override
    protected String getFXMLName() {
        return "confirm";
    }
}
