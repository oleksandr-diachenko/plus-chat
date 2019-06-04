package chat.component;

import chat.util.Paths;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomizerDialog extends AbstractDialog {

    private Paths paths;

    @Autowired
    public RandomizerDialog(Paths paths) {
        this.paths = paths;
    }


    @Override
    protected String getCSSName() {
        return paths.getRandomizerCSS();
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        //do nothing;
    }

    @Override
    protected void setStageSettings(Stage stage) {
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
}
