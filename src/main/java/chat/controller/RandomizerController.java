package chat.controller;

import chat.component.CustomListView;
import chat.component.CustomScrollPane;
import chat.component.CustomVBox;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.observer.Observer;
import chat.sevice.Bot;
import chat.util.StyleUtil;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
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
    private CustomVBox container;
    @FXML
    private CustomScrollPane scrollPane;
    @FXML
    private Label winner;
    @FXML
    private TextField keyWord;
    @FXML
    private Button play;
    @FXML
    private Label countdown;
    @FXML
    private GridPane gridPane;
    @FXML
    private CustomVBox root;
    @FXML
    private CustomListView<Integer> times;
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
        setStyles();
        setHeights();
    }

    private void setTimes() {
        times.setData(FXCollections.observableArrayList(1, 3, 5, 10, 15, 20, 30));
        times.setSelected(0);
    }

    private void setStyles() {
        styleUtil.setRootStyle(Collections.singletonList(root), applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor());
        styleUtil.setLabelsStyle(gridPane, applicationStyle.getNickColor());
    }

    private void setHeights() {
        scrollPane.bindPrefHeightProperty(root.getHeightProperty());
        scrollPane.bindValueProperty(container.getHeightProperty());
    }

    public void playAction() {
        List<Node> blankNodes = getBlankNodes(keyWord, times);
        if(blankNodes.isEmpty()) {
            Bot listener = chatController.getListener();
            listener.addObserver(this);
            play.setDisable(true);
            startTimeline(listener);
            resetContainerAndUsers();
        } else {
            blink(blankNodes);
        }
    }

    private List<Node> getBlankNodes(TextField keyWord, ListView<Integer> times) {
        List<Node> blankNodes = new LinkedList<>();
        if(keyWord.getText().isEmpty()) {
            blankNodes.add(keyWord);
        }
        if(times.getSelectionModel().isEmpty()){
            blankNodes.add(times);
        }
        return blankNodes;
    }

    private void blink(List<Node> nodes) {
        for (Node node : nodes) {
            blink(node);
        }
    }

    private void startTimeline(Bot listener) {
        Integer selectedItem = times.getSelectionModel().getSelectedItem();
        final Integer[] time = {selectedItem * 60};
        timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    time[0]--;
                    countdown.setText(String.valueOf(time[0]));
                    countdown.setStyle(styleUtil.getLabelStyle(applicationStyle.getNickColor()));
                }));
        timeline.setCycleCount(time[0]);
        timeline.setOnFinished(event -> {
            play.setDisable(false);
            listener.removeObserver(this);
        });
        timeline.play();
    }

    private void blink(Node node) {
        FadeTransition blink = new FadeTransition(Duration.seconds(0.5), node);
        blink.setFromValue(1.0);
        blink.setToValue(0.2);
        blink.setCycleCount(4);
        blink.setOnFinished(event -> node.setOpacity(1));
        blink.play();
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
                if (!users.contains(user)) {
                    Label userName = new Label(user.getCustomName());
                    userName.setStyle(styleUtil.getLabelStyle(applicationStyle.getMessageColor()));
                    container.getChildren().add(userName);
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
        play.setDisable(false);
    }

    public void selectAction() {
        winner.setText(getRandomUser().getCustomName());
        winner.setStyle(styleUtil.getLabelStyle(applicationStyle.getNickColor()));
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

    public void setTimesView(CustomListView<Integer> times) {
        this.times = times;
    }

    public void setRoot(CustomVBox root) {
        this.root = root;
    }

    public CustomVBox getRoot() {
        return root;
    }

    public void setContainer(CustomVBox container) {
        this.container = container;
    }

    public void setScrollPane(CustomScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public void setPlay(Button play) {
        this.play = play;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}
