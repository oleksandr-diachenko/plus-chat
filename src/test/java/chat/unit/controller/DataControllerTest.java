package chat.unit.controller;

import chat.component.CustomVBox;
import chat.controller.ApplicationStyle;
import chat.controller.DataController;
import chat.util.StyleUtil;
import javafx.stage.Stage;
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

    private DataController controller;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private CustomVBox root;
    @Mock
    private Stage stage;

    @Before
    public void setup() {
        controller = new DataController(styleUtil, applicationStyle);
        controller.setRoot(root);
        when(applicationStyle.getBaseColor()).thenReturn("#424242");
        when(applicationStyle.getBackgroundColor()).thenReturn("#212121");
        when(applicationStyle.getNickColor()).thenReturn("#819FF7");
    }

    @Test
    public void shouldSetRootAndLabelStyleWhenInitialize() {
        controller.initialize();

        verify(styleUtil).setRootStyle(
                Collections.singletonList(controller.getRoot()),
                applicationStyle.getBaseColor(),
                applicationStyle.getBackgroundColor()
        );
        verify(styleUtil).setLabelsStyle(root, applicationStyle.getNickColor());
    }

    @Test
    public void shouldCloseStageWhenCloseActionCalled() {
        when(root.getStage()).thenReturn(stage);

        controller.closeAction();

        verify(stage).close();
    }
}
