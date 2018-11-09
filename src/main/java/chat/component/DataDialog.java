package chat.component;

import chat.controller.DataController;
import chat.controller.SpringStageLoader;
import chat.util.*;
import insidefx.undecorator.UndecoratorScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class DataDialog {

    private final static Logger logger = LogManager.getLogger(DataDialog.class);

    private SpringStageLoader springStageLoader;
    private Paths paths;
    private Stage owner;
    private Properties settings;
    private Color fontColor;
    private Color baseColor;
    private Color backgroundColor;
    private Set<Object> objects;
    private Set<String> fields;
    private StyleUtil styleUtil;

    @Autowired
    public DataDialog(final SpringStageLoader springStageLoader, final Paths paths) {
        this.springStageLoader = springStageLoader;
        this.paths = paths;
    }

    public void openDialog(final Stage owner, final Properties settings, final Color fontColor,
                           final Color baseColor, final Color backgroundColor, final Set<Object> objects,
                           final Set<String> fields, final StyleUtil styleUtil) {
        this.owner = owner;
        this.settings = settings;
        this.fontColor = fontColor;
        this.baseColor = baseColor;
        this.backgroundColor = backgroundColor;
        this.objects = objects;
        this.fields = fields;
        this.styleUtil = styleUtil;
        final Stage stage = new Stage();
        stage.setResizable(false);
        try {
            final Region root = this.springStageLoader.load("data");
            final UndecoratorScene undecorator = getScene(stage, settings, root);
            this.styleUtil.setRootStyle(Collections.singletonList(root), ColorUtil.getHexColor(baseColor),
                    ColorUtil.getHexColor(backgroundColor));
            this.styleUtil.setLabelStyle(root, ColorUtil.getHexColor(fontColor));
            final DataController dataController = (DataController) root.getUserData();
            final TableView<Object> table = dataController.getTable();
            initData(table, objects, fields);
            stage.setScene(undecorator);
            stage.initOwner(owner);
            stage.show();
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException("Data view failed to load");
        }
    }

    private void initData(final TableView<Object> table, final Set<Object> objects, final Set<String> fields) {
        fields.forEach(field -> {
            final TableColumn<Object, Object> column = new TableColumn<>(field);
            column.setCellValueFactory(new PropertyValueFactory<>(field));
            final Callback<TableColumn<Object, Object>,
                    TableCell<Object, Object>> cellFactory = getCellFactory();
            column.setCellFactory(cellFactory);
            table.getColumns().add(column);
        });
        final ObservableList<Object> data = FXCollections.observableArrayList(objects);
        table.setItems(data);
    }

    private Callback<TableColumn<Object, Object>, TableCell<Object, Object>> getCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Object, Object> call(final TableColumn<Object, Object> param) {
                return new TableCell<>() {
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
        undecorator.getStylesheets().add(this.paths.getDataCSS());
        return undecorator;
    }
}
