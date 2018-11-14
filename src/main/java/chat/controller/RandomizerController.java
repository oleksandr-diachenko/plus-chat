package chat.controller;

import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.observer.Observer;
import chat.sevice.Bot;
import chat.util.StyleUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class RandomizerController implements Observer {

    @FXML
    private TextField keyWord;
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
    private ChatController chatController;
    private UserRepository userRepository;
    private Set<User> users = new HashSet<>();

    @Autowired
    public RandomizerController(StyleUtil styleUtil, ApplicationStyle applicationStyle,
                                ChatController chatController, UserRepository userRepository) {
        this.styleUtil = styleUtil;
        this.applicationStyle = applicationStyle;
        this.chatController = chatController;
        this.userRepository = userRepository;
    }

    @FXML
    public void initialize() {
        setTimes();
        styleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor());
        styleUtil.setLabelStyle(grid, applicationStyle.getNickColor());
    }

    private void setTimes() {
        ObservableList<Integer> data = FXCollections.observableArrayList(1, 3, 5, 10, 15, 20, 30);
        times.setItems(data);
        times.getSelectionModel().select(0);
        caseCheckbox.setSelected(true);
    }

    public void startAction() {
        Bot listener = chatController.getListener();
        Integer selectedItem = times.getSelectionModel().getSelectedItem();
        if(selectedItem == null && keyWord.getText().isEmpty()) {
            throw new RuntimeException("Time and key word must be selected!");
        }
        listener.addObserver(this);
        start.disableProperty().unbind();
        start.setDisable(true);
        final Integer[] time = {selectedItem * 60};
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    time[0]--;
                    countdown.setText(String.valueOf(time[0]));
                }));
        timeline.setCycleCount(time[0]);
        timeline.setOnFinished(event -> {
            start.setDisable(false);
            listener.removeObserver(this);
        });
        timeline.play();
    }

    private User getRandomUser() {
        int random = new Random().nextInt(users.size());
        Iterator<User> iterator = users.iterator();
        for (int index = 0; index < random; index++) {
            iterator.next();
        }
        return iterator.next();
    }

    @Override
    public void update(String nick, String message) {
        if (message.equalsIgnoreCase(keyWord.getText())) {
            Optional<User> userByName = userRepository.getUserByName(nick);
            userByName.ifPresent(user -> users.add(user));
        }
    }
}
