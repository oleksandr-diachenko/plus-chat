package chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * @author Alexander Diachenko.
 */
public class DataController {

    @FXML
    private TableView<Object> table;
    @FXML
    private VBox root;

    public TableView<Object> getTable() {
        return this.table;
    }

    public void confirmAction() {
    }

    public void cancelAction() {
    }
}
