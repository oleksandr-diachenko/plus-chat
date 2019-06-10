package chat.unit.controller;

import chat.component.CustomVBox;
import chat.component.dialog.CustomStage;
import chat.controller.ApplicationStyle;
import chat.controller.ConfirmController;
import chat.util.StyleUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmControllerTest {

    private ConfirmController controller;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private CustomVBox root;
    @Mock
    private CustomStage stage;
    @Mock
    private CustomStage owner;

    @Before
    public void setup() {
        controller = new ConfirmController(styleUtil, applicationStyle);
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
    public void shouldConfirmAndFireCloseEventAndCloseOwnerWhenConfirmActionCalled() {
        when(root.getStage()).thenReturn(stage);
        when(root.getOwner()).thenReturn(owner);
        doNothing().when(stage).fireCloseEvent();

        controller.confirmAction();

        assertTrue(controller.isConfirmed());
        verify(stage).fireCloseEvent();
        verify(owner).close();
    }

    @Test
    public void shouldFireCloseEventWhenCloseActionCalled() {
        when(root.getStage()).thenReturn(stage);
        when(root.getOwner()).thenReturn(owner);
        doNothing().when(stage).fireCloseEvent();

        controller.cancelAction();

        assertFalse(controller.isConfirmed());
        verify(stage).fireCloseEvent();
    }
}
