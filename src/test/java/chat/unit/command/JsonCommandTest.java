package chat.unit.command;

import chat.command.ICommand;
import chat.command.JSONCommand;
import chat.model.entity.Command;
import chat.model.repository.CommandRepository;
import chat.unit.factory.AbstractFactory;
import chat.unit.factory.JsonCommandFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonCommandTest {

    private ICommand command;

    @Mock
    private CommandRepository commandRepository;

    @Before
    public void setup() {
        AbstractFactory<Command> jsonCommandFactory = new JsonCommandFactory();
        when(commandRepository.getCommandByName(anyString())).thenReturn(jsonCommandFactory.create());
        command = new JSONCommand(commandRepository);
    }

    @Test
    public void shouldExecuteWhenJsonCommandCorrect() {
        boolean canExecute = command.canExecute("!fire");
        assertTrue(canExecute);
    }

    @Test
    public void shouldReturnResponseWhenJsonCommandCorrect() {
        command.canExecute("!fire");
        String execute = command.execute();
        assertEquals("Fire in the hall!", execute);
    }
}
