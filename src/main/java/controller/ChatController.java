package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author Alexander Diachenko.
 */
public class ChatController {

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
        return (Stage)scrollPane.getScene().getWindow();
    }
}
