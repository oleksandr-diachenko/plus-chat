package chat.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.stereotype.Controller;

/**
 * @author Alexander Diachenko.
 */
@Controller
public class ConfirmController {

    @FXML
    private VBox root;
    private boolean confirmed = false;

    public void confirmAction() {
        this.confirmed = true;
        final Stage stage = getStage();
        final Stage owner = getOwner();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        owner.close();
    }

    public void cancelAction() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    private Stage getStage() {
        return (Stage) this.root.getScene().getWindow();
    }

    private Stage getOwner() {
        return (Stage) getStage().getOwner();
    }
}
