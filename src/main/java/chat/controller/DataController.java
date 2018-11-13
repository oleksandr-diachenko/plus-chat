package chat.controller;

import chat.util.StyleUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
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
    private VBox root;

    @Autowired
    public DataController(StyleUtil styleUtil, ApplicationStyle applicationStyle) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
    }

    @FXML
    public void initialize() {
        this.styleUtil.setRootStyle(Collections.singletonList(this.root),
                this.applicationStyle.getBaseColor(),
                this.applicationStyle.getBackgroundColor()
        );
        this.styleUtil.setLabelStyle(this.root, this.applicationStyle.getNickColor());
    }

    public TableView<Object> getTable() {
        return this.table;
    }

    public void closeAction() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
