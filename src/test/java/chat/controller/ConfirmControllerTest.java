package chat.controller;

import chat.component.CustomStage;
import chat.component.CustomVBox;
import chat.util.StyleUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmControllerTest {

    private static final String BASE_COLOR = "#424242";
    private static final String BACKGROUND_COLOR = "#212121";
    private static final String NICK_COLOR = "#819FF7";

    @InjectMocks
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
    public void shouldConfirmAndFireCloseEventAndCloseOwnerWhenConfirmActionCalled() {
        when(root.getStage()).thenReturn(stage);
        when(root.getOwner()).thenReturn(owner);

        controller.confirmAction();

        assertTrue(controller.isConfirmed());
        verify(stage).fireCloseEvent();
        verify(owner).close();
    }

    @Test
    public void shouldFireCloseEventWhenCloseActionCalled() {
        when(root.getStage()).thenReturn(stage);

        controller.cancelAction();

        assertFalse(controller.isConfirmed());
        verify(stage).fireCloseEvent();
    }
}
