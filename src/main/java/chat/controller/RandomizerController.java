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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    private VBox container;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label winner;
    @FXML
    private TextField keyWord;
    @FXML
    private Button start;
    @FXML
    private Label countdown;
    @FXML
    private GridPane grid;
    @FXML
    private VBox root;
    @FXML
    private ListView<Integer> times;
    @FXML
    private CheckBox caseCheckbox;
    private StyleUtil styleUtil;
    private ApplicationStyle applicationStyle;
    private ChatController chatController;
    private UserRepository userRepository;
    private Set<User> users = new HashSet<>();
    private Timeline timeline;

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

        scrollPane.prefHeightProperty().bind(root.heightProperty());
        scrollPane.vvalueProperty().bind(container.heightProperty());
    }

    private void setTimes() {
        ObservableList<Integer> data = FXCollections.observableArrayList(1, 3, 5, 10, 15, 20, 30);
        times.setItems(data);
        times.getSelectionModel().select(0);
    }

    public void startAction() {
        Bot listener = chatController.getListener();
        Integer selectedItem = times.getSelectionModel().getSelectedItem();
        if (selectedItem == null && keyWord.getText().isEmpty()) {
            throw new RuntimeException("Time and key word must be selected!");
        }
        listener.addObserver(this);
        start.disableProperty().unbind();
        start.setDisable(true);
        startTimeline(listener, selectedItem);
        resetContainerAndUsers();
    }

    private void startTimeline(Bot listener, Integer selectedItem) {
        final Integer[] time = {selectedItem * 60};
        timeline = new Timeline(
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

    private void resetContainerAndUsers() {
        container.getChildren().clear();
        users = new HashSet<>();
    }

    @Override
    public void update(String nick, String message) {
        if (isEquals(message, keyWord.getText())) {
            Optional<User> userByName = userRepository.getUserByName(nick);
            userByName.ifPresent(user -> {
                if(!users.contains(user)) {
                    container.getChildren().add(new Label(user.getCustomName()));
                }
                users.add(user);
            });
        }
    }

    private boolean isEquals(String message, String keyWord) {
        boolean equals = message.equalsIgnoreCase(keyWord);
        if (caseCheckbox.isSelected()) {
            equals = message.equals(keyWord);
        }
        return equals;
    }

    public void stopAction() {
        if (timeline != null) {
            timeline.stop();
        }
        start.setDisable(false);
    }

    public void selectAction() {
        winner.setText(getRandomUser().getCustomName());
    }

    private User getRandomUser() {
        if (users.isEmpty()) {
            return new User();
        }
        int random = new Random().nextInt(users.size());
        Iterator<User> iterator = users.iterator();
        for (int index = 0; index < random; index++) {
            iterator.next();
        }
        return iterator.next();
    }
}
