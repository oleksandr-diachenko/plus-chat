package chat.command;

import chat.factory.AbstractFactory;
import chat.factory.JsonCommandFactory;
import chat.model.entity.Command;
import chat.model.repository.CommandRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonCommandTest {

    @InjectMocks
    private JSONCommand command;

    @Mock
    private CommandRepository commandRepository;

    @Before
    public void setup() {
        AbstractFactory<Command> jsonCommandFactory = new JsonCommandFactory();
        when(commandRepository.getCommandByName(anyString())).thenReturn(jsonCommandFactory.create());
    }

    @Test
    public void shouldExecuteWhenJsonCommandCorrect() {
        boolean canExecute = command.canExecute("!fire");
        assertTrue(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenJsonCommandIncorrect() {
        when(commandRepository.getCommandByName(anyString())).thenReturn(Optional.empty());
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void shouldReturnResponseWhenJsonCommandCorrect() {
        command.canExecute("!fire");
        String execute = command.execute();
        assertEquals("Fire in the hall!", execute);
    }
}
