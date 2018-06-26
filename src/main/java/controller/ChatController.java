package controller;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import sevice.ChatService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
public class ChatController {

    @FXML
    private Label start;
    @FXML
    private VBox container;
    @FXML
    private VBox root;
    @FXML
    private ScrollPane scrollPane;
    private List<Label> messages = new ArrayList<>();


    @FXML
    public void initialize() {
        scrollPane.prefHeightProperty().bind(root.heightProperty());
        try (FileInputStream fis = new FileInputStream("./img/icons/play.png")) {
            ImageView imageView = new ImageView(new Image(fis));
            imageView.setOpacity(0.4);
            start.setGraphic(imageView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAction() {
        Service service = new ChatService(container, messages);
        start.setDisable(true);
        service.restart();
    }
}
