package chat.controller;

import chat.component.MyVBox;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;

/**
 * @author Alexander Diachenko.
 */
@Controller
@NoArgsConstructor
public class DataController {

    private StyleUtil styleUtil;
    private ApplicationStyle applicationStyle;

    @FXML
    private TableView<Object> table;
    @FXML
    private MyVBox root;

    @Autowired
    public DataController(StyleUtil styleUtil, ApplicationStyle applicationStyle) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
    }

    @FXML
    public void initialize() {
        styleUtil.setRootStyle(Collections.singletonList(root),
                applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor()
        );
        styleUtil.setLabelsStyle(root, applicationStyle.getNickColor());
    }

    public TableView<Object> getTable() {
        return table;
    }

    public void closeAction() {
        getStage().close();
    }

    public Stage getStage() {
        return root.getStage();
    }

    public MyVBox getRoot() {
        return root;
    }

    public void setRoot(MyVBox root) {
        this.root = root;
    }
}
