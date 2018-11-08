package chat.component;

import chat.controller.ApplicationStyle;
import chat.controller.ConfirmController;
import chat.controller.SpringStageLoader;
import chat.util.PathsImpl;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ConfirmDialog {

    private final static Logger logger = LogManager.getLogger(ConfirmDialog.class);

    private Stage stage;
    private ConfirmController controller;
    private SpringStageLoader springStageLoader;
    private PathsImpl paths;

    public ConfirmDialog() {
        //do nothing
    }

    @Autowired
    public ConfirmDialog(final SpringStageLoader springStageLoader, final PathsImpl paths) {
        this.springStageLoader = springStageLoader;
        this.paths = paths;
    }

    public void openDialog(final Stage owner, final Properties settings, final ApplicationStyle applicationStyle) {
        this.stage = new Stage();
        this.stage.setResizable(false);
        try {
            final Region root = this.springStageLoader.load("confirm");
            this.controller = (ConfirmController) root.getUserData();
            final UndecoratorScene undecorator = getScene(settings, root);
            StyleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(), applicationStyle.getBackgroundColor());
            StyleUtil.setLabelStyle(root, applicationStyle.getNickColor());
            this.stage.setScene(undecorator);
            this.stage.initModality(Modality.WINDOW_MODAL);
            this.stage.initOwner(owner);
            this.stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Confirm view failed to load");
        }
    }

    private UndecoratorScene getScene(final Properties settings, final Region root) {
        final UndecoratorScene undecorator = new UndecoratorScene(this.stage, root);
        undecorator.getStylesheets().add(this.paths.getConfirmCSS());
        return undecorator;
    }

    public Stage getStage() {
        return this.stage;
    }

    public boolean isConfirmed() {
        return this.controller.isConfirmed();
    }
}
