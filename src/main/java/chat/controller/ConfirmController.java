package chat.controller;

import chat.component.CustomStage;
import chat.component.CustomVBox;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
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
    private CustomVBox root;
    private boolean confirmed = false;

    @Autowired
    public ConfirmController(StyleUtil styleUtil, ApplicationStyle applicationStyle) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
    }

    @FXML
    public void initialize() {
        styleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor());
        styleUtil.setLabelsStyle(root, applicationStyle.getNickColor());
    }

    public void confirmAction() {
        confirmed = true;
        CustomStage stage = root.getStage();
        stage.fireCloseEvent();
        CustomStage owner = root.getOwner();
        owner.close();
    }

    public void cancelAction() {
        CustomStage stage = root.getStage();
        stage.fireCloseEvent();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    protected void setRoot(CustomVBox root) {
        this.root = root;
    }

    protected CustomVBox getRoot() {
        return root;
    }
}
