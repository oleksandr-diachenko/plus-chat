package chat.unit.controller;

import chat.component.CustomStage;
import chat.component.CustomVBox;
import chat.controller.ApplicationStyle;
import chat.controller.DataController;
import chat.util.StyleUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataControllerTest {

    private static final String BASE_COLOR = "#424242";
    private static final String BACKGROUND_COLOR = "#212121";
    private static final String NICK_COLOR = "#819FF7";

    private DataController controller;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private CustomVBox root;
    @Mock
    private CustomStage stage;

    @Before
    public void setup() {
        controller = new DataController(styleUtil, applicationStyle);
        controller.setRoot(root);
        when(applicationStyle.getBaseColor()).thenReturn(BASE_COLOR);
        when(applicationStyle.getBackgroundColor()).thenReturn(BACKGROUND_COLOR);
        when(applicationStyle.getNickColor()).thenReturn(NICK_COLOR);
    }

    @Test
    public void shouldSetRootAndLabelStyleWhenInitialize() {
        controller.initialize();

        verify(styleUtil).setRootStyle(
                Collections.singletonList(controller.getRoot()),
                BASE_COLOR,
                BACKGROUND_COLOR
        );
        verify(styleUtil).setLabelsStyle(root, NICK_COLOR);
    }

    @Test
    public void shouldCloseStageWhenCloseActionCalled() {
        when(root.getStage()).thenReturn(stage);

        controller.closeAction();

        verify(stage).close();
    }
}
