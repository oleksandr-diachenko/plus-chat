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
    private SpringStageLoader springStageLoader;
    @Autowired
    protected Paths paths;
    private UndecoratorScene scene;
    private Stage stage;
    private Region root;

    public void openDialog(Stage owner) {
        Stage stage = new Stage();
        this.stage = stage;
        try {
            UndecoratorScene scene = getScene(stage, getRootRegion());
            stage.setScene(scene);

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

    protected abstract void setStageSettings(Stage stage);

    protected abstract void initOwner(Stage owner, Stage stage);

    protected void setEvents(Stage stage) {
        //do nothing
    }

    private Region getRootRegion() throws IOException {
        root = springStageLoader.load(getFXMLName());
        return root;
    }

    protected abstract String getFXMLName();

    private UndecoratorScene getScene(Stage stage, Region root) {
        scene = new UndecoratorScene(stage, root);
        scene.getStylesheets().add(getCSSName());
        scene.setBackgroundOpacity(0.2);
        return scene;
    }

    protected abstract String getCSSName();

    protected UndecoratorScene getScene() {
        return scene;
    }

    protected String getTitleName() {
        return "";
    }

    public Stage getStage() {
        return stage;
    }

    protected Region getRoot() {
        return root;
    }
}
