package chat.component;

import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class RandomizerDialog extends AbstractDialog {

    @Override
    protected void setStageSettings(Stage stage) {

    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        //do nothing
    }

    @Override
    protected String getTitleName() {
        return "Randomizer";
    }

    @Override
    protected String getFXMLName() {
        return "randomizer";
    }

    @Override
    protected String getCSSName() {
        return null;
    }
}
