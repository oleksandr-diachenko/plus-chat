package chat.component;

import chat.util.AppProperty;
import chat.util.ResourceBundleControl;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
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
public class SettingsDialog {

    private final static Logger logger = Logger.getLogger(SettingsDialog.class);

    public void openDialog(final Stage owner, final Node ownerRoot) {
        final Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        final Properties settings = AppProperty.getProperty("./settings/settings.properties");
        final String language = settings.getProperty("root.language");
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        final FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        try {
            final Region root = loader.load(getClass().getResourceAsStream("/view/settings.fxml"));
            stage.setOnShown(event -> {
                root.setStyle(StyleUtil.getRootStyle(settings.getProperty("root.base.color"), settings.getProperty("root.background.color")));
                final Set<Node> labels = root.lookupAll(".label");
                for (Node label : labels) {
                    label.setStyle(StyleUtil.getLabelStyle(settings.getProperty("nick.font.color")));
                }
            });

            stage.setOnCloseRequest(event -> StyleUtil.reverseStyle(settings, ownerRoot));
            final UndecoratorScene undecorator = new UndecoratorScene(stage, root);
            undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/settings.css");
            stage.setScene(undecorator);
            stage.initOwner(owner.getScene().getWindow());
            stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
    }
}
