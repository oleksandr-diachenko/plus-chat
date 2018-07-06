package chat.component;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class SettingsDialog {

    public void openDialog(Stage owner, Node ownerRoot) {
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        Properties settings = AppProperty.getProperty("./settings/settings.properties");
        String language = settings.getProperty("root.language");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        try {
            Region root = loader.load(getClass().getResourceAsStream("/view/settings.fxml"));
            stage.setOnShown(event -> {
                root.setStyle(StyleUtil.getRootStyle(settings.getProperty("root.base.color"), settings.getProperty("root.background.color")));
                Set<Node> labels = root.lookupAll(".label");
                for (Node label : labels) {
                    label.setStyle(StyleUtil.getLabelStyle(settings.getProperty("nick.font.color")));
                }
            });

            stage.setOnCloseRequest(event -> {
                StyleUtil.reverseStyle(settings, ownerRoot);
            });
            UndecoratorScene undecorator = new UndecoratorScene(stage, root);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/settings.css");
            stage.setScene(undecorator);
            stage.initOwner(owner.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
