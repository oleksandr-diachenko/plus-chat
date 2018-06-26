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
    private VBox vbox;
    @FXML
    private VBox root;
    private List<Label> messages = new ArrayList<>();
    @FXML
    private ScrollPane scrollPane;
    private static double xOffset = 0;
    private static double yOffset = 0;


    @FXML
    public void initialize() {
        scrollPane.prefHeightProperty().bind(root.heightProperty());
    }

    private Stage getStage() {
        return (Stage) scrollPane.getScene().getWindow();
    }

    public void startAction() {
        ChatService service = new ChatService(vbox, messages);
        service.restart();
    }
}
