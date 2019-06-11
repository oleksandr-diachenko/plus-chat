package chat.controller;

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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class RandomizerControllerTest {

    private static final String LABEL_COLOR_NICK = "-fx-text-fill: #819FF7;";
    private static final String BASE_COLOR = "#424242";
    private static final String BACKGROUND_COLOR = "#212121";
    private static final String NICK_COLOR = "#819FF7";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private RandomizerController controller;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private ChatController chatController;
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

    @Before
    public void setup() {
        controller = new RandomizerController(styleUtil, applicationStyle, chatController, userRepository, random);
        controller.setRoot(root);
        controller.setTimesView(times);
        controller.setContainer(container);
        controller.setScrollPane(scrollPane);
        controller.setGridPane(gridPane);
        controller.setPlay(play);
        controller.setTimeline(timeline);
        controller.setWinner(winner);
        when(applicationStyle.getBaseColor()).thenReturn(BASE_COLOR);
        when(applicationStyle.getBackgroundColor()).thenReturn(BACKGROUND_COLOR);
        when(applicationStyle.getNickColor()).thenReturn(NICK_COLOR);
        when(styleUtil.getLabelStyle(NICK_COLOR)).thenReturn(LABEL_COLOR_NICK);
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

        verify(winner).setText("customName2");
        verify(winner).setStyle(LABEL_COLOR_NICK);
    }

    private Set<User> getUsers() {
        Set<User> users = new LinkedHashSet<>();
        User user1 = new User("name1", "customName1", "firstDateMessage1", "lastDateMessage1", 0, 10);
        User user2 = new User("name2", "customName2", "firstDateMessage2", "lastDateMessage2", 15, 250);
        users.add(user1);
        users.add(user2);
        return users;
    }
}
