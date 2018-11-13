package chat.controller;

import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;

/**
 * @author Alexander Diachenko.
 */
@Controller
@NoArgsConstructor
public class ConfirmController {

    private StyleUtil styleUtil;
    private ApplicationStyle applicationStyle;

    @FXML
    private VBox root;
    private boolean confirmed = false;

    @Autowired
    public ConfirmController(StyleUtil styleUtil, ApplicationStyle applicationStyle) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
    }

    public void confirmAction() {
        this.confirmed = true;
        Stage stage = getStage();
        Stage owner = getOwner();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        owner.close();
    }

    @FXML
    public void initialize() {
        this.styleUtil.setRootStyle(Collections.singletonList(this.root), this.applicationStyle.getBaseColor(),
                this.applicationStyle.getBackgroundColor());
        this.styleUtil.setLabelStyle(this.root, this.applicationStyle.getNickColor());
    }

    public void cancelAction() {
        getStage().fireEvent(
                new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
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
