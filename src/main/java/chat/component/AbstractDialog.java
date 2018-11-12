package chat.component;

import chat.controller.SpringStageLoader;
import chat.util.Paths;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class AbstractDialog {

    private final static Logger logger = LogManager.getLogger(AbstractDialog.class);

    @Autowired
    protected SpringStageLoader springStageLoader;
    @Autowired
    protected Paths paths;
    private UndecoratorScene undecorator;
    private Stage stage;
    private Region root;

    public void openDialog(final Stage owner) {
        final Stage stage = new Stage();
        this.stage = stage;
        try {
            final UndecoratorScene undecorator = getScene(stage, getRootRegion());
            stage.setScene(undecorator);

            initOwner(owner, stage);
            setStageSettings(stage);
            setEvents(stage);

            stage.setTitle(getTitleName());
            stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException(getFXMLName() + " view failed to load", exception);
        }
    }

    protected abstract void setStageSettings(final Stage stage);

    protected abstract void initOwner(final Stage owner, final Stage stage);

    protected void setEvents(final Stage stage){
        //do nothing
    }

    private Region getRootRegion() throws IOException {
        this.root = this.springStageLoader.load(getFXMLName());
        return this.root;
    }

    protected abstract String getFXMLName();

    private UndecoratorScene getScene(final Stage stage, final Region root) {
        this.undecorator = new UndecoratorScene(stage, root);
        this.undecorator.getStylesheets().add(getCSSName());
        this.undecorator.setBackgroundOpacity(0.2);
        return this.undecorator;
    }

    protected abstract String getCSSName();

    protected UndecoratorScene getScene() {
        return this.undecorator;
    }

    protected String getTitleName(){
        return "";
    }

    public Stage getStage() {
        return this.stage;
    }

    protected Region getRoot() {
        return this.root;
    }
}
