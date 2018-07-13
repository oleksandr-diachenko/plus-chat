package chat.component;

import chat.controller.ConfirmController;
import chat.util.ColorUtil;
import chat.util.ResourceBundleControl;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class ConfirmDialog {

    private final static Logger logger = Logger.getLogger(ConfirmDialog.class);

    private Stage stage;
    private ConfirmController controller;

    public void openDialog(final Stage ownerStage, final Properties settings, final Color fontColor, final Color baseColor, final Color backgroundColor) {
        this.stage = new Stage();
        this.stage.setResizable(false);
        final String language = settings.getProperty("root.language");
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setResources(bundle);
            final Region root = fxmlLoader.load(ConfirmDialog.class.getResource("/view/confirm.fxml").openStream());
            this.controller = fxmlLoader.getController();
            final UndecoratorScene undecorator = new UndecoratorScene(this.stage, root);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/confirm.css");
            root.setStyle(StyleUtil.getRootStyle(ColorUtil.getHexColor(baseColor), ColorUtil.getHexColor(backgroundColor)));
            final Set<Node> labels = root.lookupAll(".label");
            for (Node label : labels) {
                label.setStyle(StyleUtil.getLabelStyle(ColorUtil.getHexColor(fontColor)));
            }
            this.stage.setScene(undecorator);
            this.stage.initModality(Modality.WINDOW_MODAL);
            this.stage.initOwner(ownerStage.getScene().getWindow());
            this.stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    public Stage getStage() {
        return this.stage;
    }

    public boolean isConfirmed() {
        return this.controller.isConfirmed();
    }
}
