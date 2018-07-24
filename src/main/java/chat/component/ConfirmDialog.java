package chat.component;

import chat.controller.ConfirmController;
import chat.util.ColorUtil;
import chat.util.ResourceBundleControl;
import chat.util.Settings;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Alexander Diachenko.
 */
public class ConfirmDialog {

    private final static Logger logger = LogManager.getLogger(ConfirmDialog.class);

    private Stage stage;
    private ConfirmController controller;

    public void openDialog(final Stage owner, final Properties settings, final Color fontColor, final Color baseColor, final Color backgroundColor) {
        this.stage = new Stage();
        this.stage.setResizable(false);
        final String language = settings.getProperty(Settings.ROOT_LANGUAGE);
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        try {
            final Region root = getRoot(bundle);
            this.controller = (ConfirmController) root.getUserData();
            final UndecoratorScene undecorator = getScene(settings, root);
            StyleUtil.setRootStyle(Collections.singletonList(root), ColorUtil.getHexColor(baseColor), ColorUtil.getHexColor(backgroundColor));
            StyleUtil.setLabelStyle(root, ColorUtil.getHexColor(fontColor));
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
        undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/confirm.css");
        return undecorator;
    }

    private Region getRoot(final ResourceBundle bundle) throws IOException {
        return FXMLLoader.load(getClass().getResource("/view/confirm.fxml"), bundle);
    }

    public Stage getStage() {
        return this.stage;
    }

    public boolean isConfirmed() {
        return this.controller.isConfirmed();
    }
}
