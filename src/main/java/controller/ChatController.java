package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import thread.BotThread;
import thread.Observer;

/**
 * @author Alexander Diachenko.
 */
public class ChatController implements Observer {

    static {
        BotThread botThread = new BotThread();
        new Thread(botThread).start();
    }
    @FXML
    private ScrollPane scrollPane;
    private static double xOffset = 0;
    private static double yOffset = 0;

    public ChatController() {

    }
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

    @Override
    public void update() {
        System.out.println("test");
    }
}
