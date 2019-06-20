package chat.controller;

import chat.bot.AbstractBotStarter;
import chat.component.CustomButton;
import chat.component.CustomScrollPane;
import chat.component.CustomVBox;
import chat.component.dialog.ChatDialog;
import chat.component.dialog.SettingsDialog;
import chat.model.repository.DirectRepository;
import chat.model.repository.RankRepository;
import chat.model.repository.SmileRepository;
import chat.model.repository.UserRepository;
import chat.util.AppProperty;
import chat.util.Paths;
import chat.util.StyleUtil;
import de.saxsys.javafx.test.JfxRunner;
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JfxRunner.class)
public class ChatControllerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static final String BASE_COLOR = "#424242";
    private static final String BACKGROUND_COLOR = "#212121";
    private static final String NICK_COLOR = "#819FF7";

    private ChatController controller;
    @Mock
    private RankRepository rankRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SmileRepository smileRepository;
    @Mock
    private DirectRepository directRepository;
    @Mock
    private AppProperty settingsProperty;
    @Mock
    private SettingsDialog settingsDialog;
    @Mock
    private Paths paths;
    @Mock
    private StyleUtil styleUtil;
    @Mock
    private ApplicationStyle applicationStyle;
    @Mock
    private ChatDialog chatDialog;
    private Set<AbstractBotStarter> botStarters = new HashSet<>();
    @Mock
    private CustomVBox root;
    @Mock
    private CustomVBox container;
    @Mock
    private ReadOnlyDoubleProperty roDoubleProperty;
    @Mock
    private CustomScrollPane scrollPane;
    @Mock
    private CustomButton onTop;

    @Before
    public void setup() {
        controller = new ChatController(rankRepository, userRepository, smileRepository, directRepository,
                settingsProperty, settingsDialog, paths, styleUtil, chatDialog, botStarters, applicationStyle){
            @Override
            protected void setOnTopImage() {
                //do nothing
            }
        };
        controller.setRoot(root);
        controller.setContainer(container);
        controller.setScrollPane(scrollPane);
        controller.setOnTop(onTop);
        when(settingsProperty.loadProperty()).thenReturn(getSettings());
        when(applicationStyle.getBaseColor()).thenReturn(BASE_COLOR);
        when(applicationStyle.getBackgroundColor()).thenReturn(BACKGROUND_COLOR);
        when(applicationStyle.getNickColor()).thenReturn(NICK_COLOR);
    }

    @Test
    public void shouldSetRootAndLabelStyleAndBindingWhenInitialize() {
        when(root.heightProperty()).thenReturn(roDoubleProperty);
        when(container.heightProperty()).thenReturn(roDoubleProperty);
        when(paths.getEnabledPin()).thenReturn("qwe");

        controller.initialize();

        verify(styleUtil).setRootStyle(
                Collections.singletonList(controller.getRoot()),
                BASE_COLOR,
                BACKGROUND_COLOR
        );
        verify(chatDialog).setOpacity(1);
        verify(scrollPane).bindPrefHeightProperty(roDoubleProperty);
        verify(scrollPane).bindValueProperty(roDoubleProperty);
    }

    private Properties getSettings() {
        Properties settings = new Properties();
        settings.put("root.background.transparency", "100");
        settings.put("sound.direct.message", "direct-message.wav");
        settings.put("font.size", "17");
        settings.put("root.base.color", "\\#424242");
        settings.put("font.direct.message.color", "\\#C15722");
        settings.put("font.message.color", "\\#9E9 E9E");
        settings.put("root.language", "ru");
        settings.put("sound.message.volume", "100");
        settings.put("sound.direct.message.volume", "100");
        settings.put("font.separator.color", "\\#819F F7");

        settings.put("root.always.on.top", "true");
        settings.put("root.theme", "default");
        settings.put(" message.max.displayed", "100");
        settings.put(" font.nick.color", "\\#819F F7");
        settings.put(" sound.enable", "false");
        settings.put("sound.message", "message.wav");
        settings.put("root.background.color", "\\#212121");
        return settings;
    }
}
