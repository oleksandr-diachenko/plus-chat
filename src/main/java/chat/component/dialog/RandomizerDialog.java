package chat.component.dialog;

import chat.component.CustomStage;
import chat.util.Paths;
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
    protected void initOwner(CustomStage owner, CustomStage stage) {
        //do nothing;
    }

    @Override
    protected void setStageSettings(CustomStage stage) {
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
