package chat.component;

import chat.controller.DataController;
import chat.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class DataDialog extends AbstractDialog {

    private Set<String> fields;
    private Set<Object> data;

    @Override
    protected void setStageSettings(final Stage stage) {
        stage.setResizable(false);
        final DataController dataController = (DataController) getRoot().getUserData();
        final TableView<Object> table = dataController.getTable();
        initData(table, data, fields);
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

    @Override
    protected void initOwner(final Stage owner, final Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected String getFXMLName() {
        return "data";
    }

    @Override
    protected String getCSSName() {
        return this.paths.getDataCSS();
    }

    public void setTableFields(final Set<String> fields) {
        this.fields = fields;
    }

    public void setTableData(final Set<Object> data) {
        this.data = data;
    }
}
