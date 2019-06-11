package chat.command;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author Oleksandr_Diachenko
 */
public class UpCommandTest {

    private ICommand command;

    @Before
    public void setup() {
        command = new UpCommand(LocalDateTime.now());
    }

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