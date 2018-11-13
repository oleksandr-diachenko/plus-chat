package chat.controller;

import chat.util.StyleUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class RandomizerController {

    @FXML
    private Button start;
    @FXML
    private Label countdown;
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
        ObservableList<Integer> data = FXCollections.observableArrayList(1, 3, 5, 10, 15, 20, 30);
        times.setItems(data);
        caseCheckbox.setSelected(true);

        styleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor());
        styleUtil.setLabelStyle(grid, applicationStyle.getNickColor());
    }

    public void startAction() {
        start.setDisable(true);
        final Integer[] time = {times.getSelectionModel().getSelectedItem() * 60};
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), ae -> {
                    time[0]--;
                    countdown.setText(String.valueOf(time[0]));
                }
                )
        );
        timeline.setCycleCount(time[0]);
        timeline.setOnFinished(event -> {
            start.setDisable(false);
        });
        timeline.play();
    }
}
