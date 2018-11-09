package chat.component;

import chat.controller.ConfirmController;
import chat.controller.SpringStageLoader;
import chat.util.Paths;
import insidefx.undecorator.UndecoratorScene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Alexander Diachenko.
 */
@Component
public class ConfirmDialog {

    private final static Logger logger = LogManager.getLogger(ConfirmDialog.class);

    private Stage stage;
    private ConfirmController controller;
    private SpringStageLoader springStageLoader;
    private Paths paths;

    public ConfirmDialog() {
        //do nothing
    }

    @Autowired
    public ConfirmDialog(final SpringStageLoader springStageLoader, final Paths paths) {
        this.springStageLoader = springStageLoader;
        this.paths = paths;
    }

    public void openDialog(final Stage owner) {
        this.stage = new Stage();
        try {
            final Region root = getRoot();
            final UndecoratorScene undecorator = getScene(root);

            this.controller = (ConfirmController) root.getUserData();
            this.stage.setResizable(false);

            this.stage.setScene(undecorator);
            this.stage.initModality(Modality.WINDOW_MODAL);
            this.stage.initOwner(owner);
            this.stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Confirm view failed to load");
        }
    }

    private Region getRoot() throws IOException {
        return this.springStageLoader.load("confirm");
    }

    private UndecoratorScene getScene(final Region root) {
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
