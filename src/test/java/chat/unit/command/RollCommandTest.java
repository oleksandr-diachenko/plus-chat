package chat.unit.command;

import chat.command.ICommand;
import chat.command.RollCommand;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import chat.unit.factory.AbstractFactory;
import chat.unit.factory.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
@PrepareForTest(RollCommand.class)
public class RollCommandTest {

    private ICommand command;
    //TODO подружить PowerMockRunner и SpringJUnit4ClassRunner
    private AbstractFactory<User> userFactory = new UserFactory();
    private Random random;

    @Before
    public void setup() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        random = mock(Random.class);
        whenNew(Random.class).withNoArguments().thenReturn(random);
        command = new RollCommand(userRepository, "p0sltlv");
    }

    @Test
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!roll 10");
        assertTrue(canExecute);
    }

    @Test
    public void canExecute_wrongArgumentCount_oneArgumentTest() {
        boolean canExecute = command.canExecute("!roll");
        assertFalse(canExecute);
    }

    @Test
    public void canExecute_wrongArgument_secondArgumentNotNumericTest() {
        boolean canExecute = command.canExecute("!roll qwe");
        assertFalse(canExecute);
    }

    @Test
    public void executeTest_randomTenLost() {
        command.canExecute("!roll 10");
        when(random.nextInt(anyInt())).thenReturn(10);
        String execute = command.execute();
        assertEquals("POSITIV, you lost 10 points! (You have 990 points)", execute);
    }

    @Test
    public void executeTest_random100Won() {
        command.canExecute("!roll 30");
        when(random.nextInt(anyInt())).thenReturn(100);
        when(random.nextFloat()).thenReturn((float) 0.5);
        String execute = command.execute();
        assertEquals("POSITIV, you won 45 points! (You have 1045 points)", execute);
    }

    @Test
    public void executeTest_random75Lost() {
        command.canExecute("!roll 100");
        when(random.nextInt(anyInt())).thenReturn(75);
        String execute = command.execute();
        assertEquals("POSITIV, you lost 100 points! (You have 900 points)", execute);
    }

    @Test
    public void executeTest_random76Won() {
        command.canExecute("!roll 100");
        when(random.nextInt(anyInt())).thenReturn(76);
        when(random.nextFloat()).thenReturn((float) 0.5);
        String execute = command.execute();
        assertEquals("POSITIV, you won 150 points! (You have 1150 points)", execute);
    }
}
