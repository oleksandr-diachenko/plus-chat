package chat.component;

import chat.controller.DataController;
import chat.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private StyleUtil styleUtil;

    @Override
    protected void setStageSettings(Stage stage) {
        stage.setResizable(false);
        DataController dataController = (DataController) getRoot().getUserData();
        TableView<Object> table = dataController.getTable();
        initData(table, data, fields);
    }

    private void initData(TableView<Object> table, Set<Object> objects, Set<String> fields) {
        fields.forEach(field -> {
            TableColumn<Object, Object> column = new TableColumn<>(getFormatted(field));
            column.setCellValueFactory(new PropertyValueFactory<>(field));
            Callback<TableColumn<Object, Object>,
                    TableCell<Object, Object>> cellFactory = getCellFactory();
            column.setCellFactory(cellFactory);
            table.getColumns().add(column);
        });
        ObservableList<Object> data = FXCollections.observableArrayList(objects);
        table.setItems(data);
    }

    private String getFormatted(String fieldName) {
        String[] words = fieldName.split("(?=\\p{Upper})");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word.toLowerCase()).append(" ");
        }
        return builder.toString().trim();
    }

    private Callback<TableColumn<Object, Object>, TableCell<Object, Object>> getCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Object, Object> call(TableColumn<Object, Object> param) {
                return new TableCell<>() {
                    Text text = new Text();
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            String string = item.toString();
                            text.setText(StringUtil.getUTF8String(string));
                            text.setStyle(styleUtil.getTextStyle("12", "#474747"));
                            text.setWrappingWidth(600 / fields.size());
                            setGraphic(text);
                        }
                    }
                };
            }
        };
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected String getFXMLName() {
        return "data";
    }

    @Override
    protected String getCSSName() {
        return paths.getDataCSS();
    }

    public void setTableFields(Set<String> fields) {
        this.fields = fields;
    }

    public void setTableData(Set<Object> data) {
        this.data = data;
    }
}
