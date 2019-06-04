package chat.component;

import chat.controller.DataController;
import chat.util.StringUtil;
import chat.util.StyleUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
@Component
@NoArgsConstructor
public class DataDialog extends AbstractDialog {

    private static final String FONT_SIZE = "12";
    private static final String FONT_COLOR = "#474747";
    private static final int WRAPPING_WIDTH = 140;
    private Set<String> fields;
    private Set<Object> data;
    private StyleUtil styleUtil;
    private DataController dataController;

    @Autowired
    public DataDialog(StyleUtil styleUtil, DataController dataController) {
        this.styleUtil = styleUtil;
        this.dataController = dataController;
    }

    @Override
    protected void initOwner(Stage owner, Stage stage) {
        stage.initOwner(owner);
    }

    @Override
    protected void setStageSettings(Stage stage) {
        stage.setResizable(false);
        TableView<Object> table = dataController.getTable();
        initData(table);
    }

    @Override
    protected String getFXMLName() {
        return "data";
    }

    @Override
    protected String getCSSName() {
        return paths.getDataCSS();
    }

    private void initData(TableView<Object> table) {
        fields.forEach(field -> {
            TableColumn<Object, Object> column = new TableColumn<>(getFormatted(field));
            column.setCellValueFactory(new PropertyValueFactory<>(field));
            column.setCellFactory(cell -> getTableCell());
            table.getColumns().add(column);
        });
        table.setItems(FXCollections.observableArrayList(data));
    }

    private String getFormatted(String field) {
        String[] words = getWordsByUpperCaseSplit(field);
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word.toLowerCase()).append(" ");
        }
        return builder.toString().trim();
    }

    private String[] getWordsByUpperCaseSplit(String field) {
        return field.split("(?=\\p{Upper})");
    }

    private TableCell<Object, Object> getTableCell() {
        return new TableCell<>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Text text = new Text();
                    String string = item.toString();
                    text.setText(StringUtil.getUTF8String(string));
                    text.setStyle(styleUtil.getTextStyle(FONT_SIZE, FONT_COLOR));
                    text.setWrappingWidth(WRAPPING_WIDTH);
                    setGraphic(text);
                }
            }
        };
    }

    public void setTableFields(Set<String> fields) {
        this.fields = fields;
    }

    public void setTableData(Set<Object> data) {
        this.data = data;
    }
}
