package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private Pane pane;
    @FXML
    private VBox root;
    private List<HBox> messages = new ArrayList<>();
    @FXML
    private ScrollPane scrollPane;
    private static double xOffset = 0;
    private static double yOffset = 0;


    @FXML
    public void initialize() {
        scrollPane.prefHeightProperty().bind(root.heightProperty());
    }
    public void closeAction() {
        Platform.exit();
    }

    public void hideAction() {
        getStage().setIconified(true);
    }

    private Stage getStage() {
        return (Stage) scrollPane.getScene().getWindow();
    }

    public void startAction() {
        ChatService service = new ChatService(scrollPane, messages);
        service.restart();
    }
}
