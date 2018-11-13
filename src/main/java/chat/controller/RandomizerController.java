package chat.controller;

import chat.util.StyleUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class RandomizerController {

    @FXML
    private GridPane grid;
    @FXML
    private Node root;
    @FXML
    private ListView<Integer> times;
    @FXML
    private CheckBox caseCheckbox;
    private StyleUtil styleUtil;
    private ApplicationStyle applicationStyle;

    @Autowired
    public RandomizerController(StyleUtil styleUtil, ApplicationStyle applicationStyle) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
    }

    @FXML
    public void initialize() {
        ObservableList<Integer> data = FXCollections.observableArrayList (1, 3, 5, 10, 15, 20, 30);
        times.setItems(data);
        caseCheckbox.setSelected(true);

        styleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor());
        styleUtil.setLabelStyle(grid, applicationStyle.getNickColor());
    }

    public void countdownAction() {

    }
}
