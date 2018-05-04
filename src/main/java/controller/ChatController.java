package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import thread.Observer;

/**
 * @author Alexander Diachenko.
 */
public class ChatController implements Observer {

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

    @Override
    public void update(String user, String message) {
        Platform.runLater(() -> label.setText(user + " : " + message));
    }
}
