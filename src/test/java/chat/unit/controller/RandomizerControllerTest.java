package chat.unit.controller;

import chat.component.CustomListView;
import chat.component.CustomScrollPane;
import chat.component.CustomVBox;
import chat.controller.ApplicationStyle;
import chat.controller.ChatController;
import chat.controller.RandomizerController;
import chat.model.repository.UserRepository;
import chat.util.StyleUtil;
import de.saxsys.javafx.test.JfxRunner;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class RandomizerControllerTest {

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

    @Before
    public void setup() {
        controller = new RandomizerController(styleUtil, applicationStyle, chatController, userRepository);
        controller.setRoot(root);
        controller.setTimesView(times);
        controller.setContainer(container);
        controller.setScrollPane(scrollPane);
        controller.setGridPane(gridPane);
        controller.setPlay(play);
        controller.setTimeline(timeline);
        when(applicationStyle.getBaseColor()).thenReturn("#424242");
        when(applicationStyle.getBackgroundColor()).thenReturn("#212121");
        when(applicationStyle.getNickColor()).thenReturn("#819FF7");
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
}
