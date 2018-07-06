package chat.component;

import chat.controller.ConfirmController;
import chat.util.ColorUtil;
import chat.util.ResourceBundleControl;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class ConfirmDialog {

    private Stage stage;
    private ConfirmController controller;

    public void openDialog(Stage ownerStage, Properties settings, Color fontColor, Color baseColor, Color backgroundColor) {
        stage = new Stage();
        stage.setResizable(false);
        String language = settings.getProperty("root.language");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        Region root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setResources(bundle);
            root = fxmlLoader.load(ConfirmDialog.class.getResource("/view/dialog.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = fxmlLoader.getController();
        UndecoratorScene undecorator = new UndecoratorScene(stage, root);
        undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/dialog.css");
        root.setStyle(StyleUtil.getRootStyle(ColorUtil.getHexColor(baseColor), ColorUtil.getHexColor(backgroundColor)));
        Set<Node> labels = root.lookupAll(".label");
        for (Node label : labels) {
            label.setStyle(StyleUtil.getLabelStyle(ColorUtil.getHexColor(fontColor)));
        }
        stage.setScene(undecorator);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ownerStage.getScene().getWindow());
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isConfirmed() {
        return controller.isConfirmed();
    }
}
