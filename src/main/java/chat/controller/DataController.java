package chat.controller;

import chat.component.CustomVBox;
import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
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
    private CustomVBox root;

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
        root.getStage().close();
    }

    public CustomVBox getRoot() {
        return root;
    }

    public void setRoot(CustomVBox root) {
        this.root = root;
    }
}
