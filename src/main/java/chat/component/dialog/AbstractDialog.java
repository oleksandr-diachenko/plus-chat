package chat.component.dialog;

import chat.component.CustomStage;
import chat.controller.SpringStageLoader;
import chat.util.Paths;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.layout.Region;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
    private CustomStage stage;
    private Region root;

    public void openDialog(CustomStage owner) {
        stage = new CustomStage();
        try {
            showStage(owner, stage);
        } catch (IOException exception) {
            log(exception);
        }
    }

    private void log(IOException exception) {
        log.error(exception.getMessage(), exception);
        throw new RuntimeException(getFXMLName() + " view failed to load", exception);
    }

    private void showStage(CustomStage owner, CustomStage stage) throws IOException {
        loadRoot();
        createScene(stage, root);
        stage.setScene(scene);

        initOwner(owner, stage);
        setStageSettings(stage);
        setEvents(stage);

        stage.setTitle(getTitleName());
        stage.show();
    }

    protected UndecoratorScene getScene() {
        return scene;
    }

    protected abstract String getCSSName();

    protected abstract void initOwner(CustomStage owner, CustomStage stage);

    protected abstract void setStageSettings(CustomStage stage);

    protected void setEvents(CustomStage stage) {
        //do nothing
    }

    protected String getTitleName() {
        return StringUtils.EMPTY;
    }

    protected abstract String getFXMLName();

    public CustomStage getStage() {
        return stage;
    }

    protected Region getRoot() {
        return root;
    }

    private void loadRoot() throws IOException {
        root = springStageLoader.load(getFXMLName());
    }

    private void createScene(CustomStage stage, Region root) {
        scene = new UndecoratorScene(stage, root);
        scene.getStylesheets().add(getCSSName());
        scene.setBackgroundOpacity(0.2);
    }
}
