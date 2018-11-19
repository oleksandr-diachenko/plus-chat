package chat.unit.command;

import chat.command.ICommand;
import chat.command.JSONCommand;
import chat.model.entity.Command;
import chat.model.repository.CommandRepository;
import chat.unit.factory.AbstractFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JsonCommandTest {

    private ICommand command;
    @Autowired
    private AbstractFactory<Command> jsonCommandFactory;

    @Before
    public void setup() {
        CommandRepository commandRepository = mock(CommandRepository.class);
        when(commandRepository.getCommandByName(anyString())).thenReturn(jsonCommandFactory.create());
        command = new JSONCommand(commandRepository);
    }

    @Test
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!fire");
        assertTrue(canExecute);
    }

    @Test
    public void executeTest() {
        command.canExecute("!fire");
        String execute = command.execute();
        assertEquals("Fire in the hall!", execute);
    }
}
