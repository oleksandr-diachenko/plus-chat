package chat.component;

import chat.controller.SpringStageLoader;
import chat.util.Paths;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public abstract class AbstractDialog {

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
            log.error(exception.getMessage(), exception);
            throw new RuntimeException(getFXMLName() + " view failed to load", exception);
        }
    }

    protected UndecoratorScene getScene() {
        return scene;
    }

    protected abstract String getCSSName();

    protected abstract void initOwner(Stage owner, Stage stage);

    protected abstract void setStageSettings(Stage stage);

    protected void setEvents(Stage stage) {
        //do nothing
    }

    protected String getTitleName() {
        return "";
    }

    protected abstract String getFXMLName();

    public Stage getStage() {
        return stage;
    }

    protected Region getRoot() {
        return root;
    }

    private Region getRootRegion() throws IOException {
        root = springStageLoader.load(getFXMLName());
        return root;
    }

    private UndecoratorScene getScene(Stage stage, Region root) {
        scene = new UndecoratorScene(stage, root);
        scene.getStylesheets().add(getCSSName());
        scene.setBackgroundOpacity(0.2);
        return scene;
    }
}
