package chat.component;

import chat.controller.DataController;
import chat.util.*;
import insidefx.undecorator.UndecoratorScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class DataDialog {

    private final static Logger logger = LogManager.getLogger(DataDialog.class);

    public void openDialog(final Stage owner, final Properties settings, final Color fontColor, final Color baseColor,
                           final Color backgroundColor, final Set<Object> objects, final Set<String> fields) {
        final Stage stage = new Stage();
        stage.setResizable(false);
        final String language = settings.getProperty(Settings.ROOT_LANGUAGE);
        final ResourceBundle bundle = ResourceBundle.getBundle("bundles.chat", new Locale(language), new ResourceBundleControl());
        try {
            final Region root = getRoot(bundle);
            final UndecoratorScene undecorator = getScene(stage, settings, root);
            StyleUtil.setRootStyle(Collections.singletonList(root), ColorUtil.getHexColor(baseColor), ColorUtil.getHexColor(backgroundColor));
            StyleUtil.setLabelStyle(root, ColorUtil.getHexColor(fontColor));
            final DataController dataController = (DataController) root.getUserData();
            final TableView<Object> table = dataController.getTable();
            initData(table, objects, fields);
            stage.setScene(undecorator);
            stage.initOwner(owner);
            stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
    }

    private void initData(final TableView<Object> table, final Set<Object> objects, final Set<String> fields) {
        for (String field : fields) {
            final TableColumn<Object, Object> column = new TableColumn<>(field);
            column.setCellValueFactory(new PropertyValueFactory<>(field));
            final Callback<TableColumn<Object, Object>, TableCell<Object, Object>> cellFactory = getCellFactory();
            column.setCellFactory(cellFactory);
            table.getColumns().add(column);
        }
        final ObservableList<Object> data = FXCollections.observableArrayList(objects);
        table.setItems(data);
    }

    private Callback<TableColumn<Object, Object>, TableCell<Object, Object>> getCellFactory() {
        return new Callback<TableColumn<Object, Object>, TableCell<Object, Object>>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<Object, Object>() {
                    @Override
                    public void updateItem(final Object item, final boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setTextFill(Color.web("#474747"));
                            setText(StringUtil.getUTF8String(item.toString()));
                        }
                    }
                };
            }
        };
    }

    private UndecoratorScene getScene(final Stage stage, final Properties settings, final Region root) {
        final UndecoratorScene undecorator = new UndecoratorScene(stage, root);
        undecorator.getStylesheets().add("/theme/" + settings.getProperty(Settings.ROOT_THEME) + "/data.css");
        return undecorator;
    }

    private Region getRoot(final ResourceBundle bundle) throws IOException {
        return FXMLLoader.load(getClass().getResource("/view/data.fxml"), bundle);
    }
}
