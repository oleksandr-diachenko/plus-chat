package chat.unit.command;

import chat.command.ICommand;
import chat.command.UpCommand;
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
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!up");
        assertTrue(canExecute);
    }

    @Test
    public void canExecute_wrongCommandTest() {
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void executeTest() {
        String execute = command.execute();
        assertEquals("00h 00m 00s", execute);
    }
}