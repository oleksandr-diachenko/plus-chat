package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sevice.ChatService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
public class ChatController {

    @FXML
    private Label stop;
    @FXML
    private Label start;
    @FXML
    private VBox vbox;
    @FXML
    private VBox root;
    private List<Label> messages = new ArrayList<>();
    @FXML
    private ScrollPane scrollPane;
    private ChatService service;


    @FXML
    public void initialize() {
        scrollPane.prefHeightProperty().bind(root.heightProperty());
    }

    private Stage getStage() {
        return (Stage) scrollPane.getScene().getWindow();
    }

    public void startAction() {
        start.setDisable(true);
        stop.setDisable(false);
        service = new ChatService(vbox, messages);
        service.restart();
    }

    public void stopAction() {
        start.setDisable(false);
        stop.setDisable(true);
        service.cancel();
    }
}
