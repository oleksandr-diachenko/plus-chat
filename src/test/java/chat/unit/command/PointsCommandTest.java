package chat.unit.command;

import chat.command.ICommand;
import chat.command.PointsCommand;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.unit.factory.AbstractFactory;
import chat.unit.factory.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class PointsCommandTest {

    private ICommand command;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        AbstractFactory<User> userFactory = new UserFactory();
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        command = new PointsCommand(userRepository, "p0sltlv");
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