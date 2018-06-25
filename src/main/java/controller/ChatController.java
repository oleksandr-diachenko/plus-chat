package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sevice.ChatService;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
public class ChatController{

    private List<String> messages = new LinkedList<>();
    @FXML
    private Label label;
    @FXML
    private ScrollPane scrollPane;
    private static double xOffset = 0;
    private static double yOffset = 0;

    public void closeAction() {
        Platform.exit();
    }

    public void hideAction() {
        getStage().setIconified(true);
    }

    public void splitOnMousePressed(MouseEvent event) {
        xOffset = getStage().getX() - event.getScreenX();
        yOffset = getStage().getY() - event.getScreenY();
    }

    public void splitOnMouseDragged(MouseEvent event) {
        getStage().setX(event.getScreenX() + xOffset);
        getStage().setY(event.getScreenY() + yOffset);
    }

    private Stage getStage() {
        return (Stage) scrollPane.getScene().getWindow();
    }

    public List<String> getMessages() {
        return messages;
    }

    public Label getLabel() {
        return label;
    }

    public void startAction() {
        ChatService service = new ChatService(label);
        service.restart();
    }
}
