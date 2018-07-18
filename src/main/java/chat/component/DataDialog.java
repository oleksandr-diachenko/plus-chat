package chat.component;

import chat.controller.DataController;
import chat.util.ColorUtil;
import chat.util.ResourceBundleControl;
import chat.util.StyleUtil;
import insidefx.undecorator.UndecoratorScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class DataDialog {

    private final static Logger logger = Logger.getLogger(DataDialog.class);

    private Stage stage;

    public void openDialog(final Stage ownerStage, final Properties settings, final Color fontColor, final Color baseColor,
                           final Color backgroundColor, final Set<Object> objects, final Set<String> fields) {
        this.stage = new Stage();
        this.stage.setResizable(false);
        final String language = settings.getProperty("root.language");
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        try {
            final Region root = getRoot(bundle);
            final UndecoratorScene undecorator = getScene(settings, root);
            StyleUtil.setRootStyle(Collections.singletonList(root), ColorUtil.getHexColor(baseColor), ColorUtil.getHexColor(backgroundColor));
            StyleUtil.setLabelStyle(root, ColorUtil.getHexColor(fontColor));
            final DataController dataController = (DataController) root.getUserData();
            final TableView<Object> table = dataController.getTable();
            initData(table, objects, fields);
            this.stage.setScene(undecorator);
            this.stage.initOwner(ownerStage.getScene().getWindow());
            this.stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    private void initData(final TableView<Object> table, final Set<Object> objects, final Set<String> fields) {
        for (String field : fields) {
            final TableColumn<Object, String> column = new TableColumn<>(field);
            column.setCellValueFactory(new PropertyValueFactory<>(field));
            table.getColumns().add(column);
        }
        final ObservableList<Object> data = FXCollections.observableArrayList(objects);
        table.setItems(data);
    }

    private UndecoratorScene getScene(final Properties settings, final Region root) {
        final UndecoratorScene undecorator = new UndecoratorScene(this.stage, root);
        undecorator.getStylesheets().add("/theme/" + settings.getProperty("root.theme") + "/confirm.css");
        return undecorator;
    }

    private Region getRoot(final ResourceBundle bundle) throws IOException {
        return FXMLLoader.load(getClass().getResource("/view/data.fxml"), bundle);
    }
}
