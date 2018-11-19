package chat.unit.command;

import chat.command.ICommand;
import chat.command.PointsCommand;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.unit.factory.AbstractFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PointsCommandTest {

    private ICommand command;
    @Autowired
    private AbstractFactory<User> userFactory;

    @Before
    public void setup() {
        UserRepository userRepository = mock(UserRepository.class);
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