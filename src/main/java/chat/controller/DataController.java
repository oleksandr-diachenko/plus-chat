package chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * @author Alexander Diachenko.
 */
public class DataController {

    @FXML
    private TableView table;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox root;

    public TableView getTable() {
        return table;
    }
}
