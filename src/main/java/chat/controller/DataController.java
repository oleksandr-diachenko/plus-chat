package chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    public void closeAction() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
