package chat.controller;

import chat.bot.Startable;
import chat.component.CustomListView;
import chat.component.CustomScrollPane;
import chat.component.CustomVBox;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.util.StyleUtil;
import de.saxsys.javafx.test.JfxRunner;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class RandomizerControllerTest {

    private static final String BASE_COLOR = "#424242";
    private static final String BACKGROUND_COLOR = "#212121";
    private static final String NICK_COLOR = "#819FF7";
    private static final String MESSAGE_COLOR = "#9E9E9E";
    private static final String LABEL_COLOR_NICK = "-fx-text-fill: #819FF7;";
    private static final String LABEL_COLOR_MESSAGE = "-fx-text-fill: #9E9E9E;";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private RandomizerController controller;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Random random;
    @Mock
    private CustomVBox container;
    @Mock
    private CustomListView<Integer> times;
    @Mock
    private CustomVBox root;
    @Mock
    private CustomScrollPane scrollPane;
    @Mock
    private ReadOnlyDoubleProperty roDoubleProperty;
    @Mock
    private GridPane gridPane;
    @Mock
    private Button play;
    @Mock
    private Timeline timeline;
    @Mock
    private Label winner;
    @Mock
    private TextField keyWord;
    @Mock
    private CheckBox caseCheckbox;
    private Set<Startable> startables = new HashSet<>();

    @Before
    public void setup() {
        controller = new RandomizerController(styleUtil, applicationStyle, userRepository, startables);
        controller.setRoot(root);
        controller.setTimesView(times);
        controller.setContainer(container);
        controller.setScrollPane(scrollPane);
        controller.setGridPane(gridPane);
        controller.setPlay(play);
        controller.setTimeline(timeline);
        controller.setWinner(winner);
        controller.setKeyWord(keyWord);
        controller.setCaseCheckbox(caseCheckbox);
        controller.setRandom(random);
        when(applicationStyle.getBaseColor()).thenReturn(BASE_COLOR);
        when(applicationStyle.getBackgroundColor()).thenReturn(BACKGROUND_COLOR);
        when(applicationStyle.getNickColor()).thenReturn(NICK_COLOR);
        when(applicationStyle.getMessageColor()).thenReturn(MESSAGE_COLOR);
        when(styleUtil.getLabelStyle(NICK_COLOR)).thenReturn(LABEL_COLOR_NICK);
        when(styleUtil.getLabelStyle(MESSAGE_COLOR)).thenReturn(LABEL_COLOR_MESSAGE);
    }

    @Test
    public void shouldSetRootAndLabelStyleAndBindingWhenInitialize() {
        doNothing().when(times).setData(any());
        when(root.getHeightProperty()).thenReturn(roDoubleProperty);
        doNothing().when(scrollPane).bindPrefHeightProperty(roDoubleProperty);
        when(container.getHeightProperty()).thenReturn(roDoubleProperty);
        doNothing().when(scrollPane).bindValueProperty(roDoubleProperty);

        controller.initialize();

        assertEquals(times.getSelected(), 0);
        verify(styleUtil).setRootStyle(
                Collections.singletonList(controller.getRoot()),
                applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor()
        );
        verify(styleUtil).setLabelsStyle(gridPane, applicationStyle.getNickColor());
        verify(scrollPane).bindPrefHeightProperty(roDoubleProperty);
        verify(scrollPane).bindValueProperty(roDoubleProperty);
    }

    @Test
    public void shouldStopTimelineWhenStopActionCalled() {
        controller.stopAction();

        verify(timeline).stop();
        assertFalse(play.isDisable());
    }

    @Test
    public void shouldSetRandomUserAndSetStyleForUserWhenSelectActionCalled() {
        controller.setUsers(getUsers());
        when(random.nextInt(anyInt())).thenReturn(1);

        controller.selectAction();

        verify(winner).setText("Greyphan");
        verify(winner).setStyle(LABEL_COLOR_NICK);
    }

    @Test
    public void shouldAddUserToContainerAndSetStyleWhenUpdateCalledWithNewUser() {
        when(keyWord.getText()).thenReturn("!key");
        when(caseCheckbox.isSelected()).thenReturn(true);
        when(userRepository.getUserByName("positiv")).thenReturn(Optional.of(getPositiv()));

        controller.update("positiv", "!key");

        Label userName = controller.getUserName(getPositiv().getCustomName());
        assertEquals(LABEL_COLOR_MESSAGE, userName.getStyle());
        verify(container).addNode(any(Label.class));
        assertFalse(controller.getUsers().isEmpty());
    }

    @Test
    public void shouldNotAddUserToContainerWhenUpdateCalledWithExistingUser() {
        when(keyWord.getText()).thenReturn("!key");
        when(caseCheckbox.isSelected()).thenReturn(true);
        when(userRepository.getUserByName("positiv")).thenReturn(Optional.of(getPositiv()));
        controller.setUsers(getUsers());

        controller.update("positiv", "!key");

        verify(container, never()).addNode(any(Label.class));
    }

    private Set<User> getUsers() {
        Set<User> users = new LinkedHashSet<>();
        users.add(getPositiv());
        users.add(getGreyphan());
        return users;
    }

    private User getGreyphan() {
        return new User("greyphan", "Greyphan", "firstDateMessage2", "lastDateMessage2", 15, 250);
    }

    private User getPositiv() {
        return new User("positiv", "POSITIV", "firstDateMessage1", "lastDateMessage1", 0, 10);
    }
}
