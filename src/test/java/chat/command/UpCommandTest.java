package chat.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class UpCommandTest {

    @InjectMocks
    private UpCommand command;

    @Mock
    private LocalDateTime localDateTime;

    @Test
    public void shouldExecuteWhenUpCommandCorrect() {
        boolean canExecute = command.canExecute("!up");
        assertTrue(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenUpCommandIncorrect() {
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void shouldReturnResponseWhenUpCommandCorrect() {
        String execute = command.execute();
        assertEquals("00h 00m 00s", execute);
    }
}