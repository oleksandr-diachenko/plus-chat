package chat.unit.command;

import chat.command.ICommand;
import chat.command.PointsCommand;
import chat.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
public class PointsCommandTest {

    private ICommand command;

    @Before
    public void setup() {
        UserRepository userRepository = mock(UserRepository.class);
        String userName = "p0sltlv";
        when(userRepository.getUserByName(userName)).thenReturn(UserFactory.createUser());
        command = new PointsCommand(userRepository, userName);
    }

    @Test
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!points");
        assertTrue(canExecute);
    }

    @Test
    public void canExecute_wrongCommandTest() {
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void executeTest() {
        command.canExecute("p0sltlv");
        String execute = command.execute();
        assertEquals("POSITIV, you have 1000 points.", execute);
    }
}