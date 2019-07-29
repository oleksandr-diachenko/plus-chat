package chat.command;

import chat.factory.AbstractFactory;
import chat.factory.UserFactory;
import chat.model.entity.User;
import chat.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class RollCommandTest {

    @InjectMocks
    private RollCommand command;

    @Mock
    private Random random;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        AbstractFactory<User> userFactory = new UserFactory();
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        command.setNick("p0sltlv");
    }

    @Test
    public void shouldExecuteWhenRollCommandCorrect() {
        boolean canExecute = command.canExecute("!roll 10");
        assertTrue(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenRollCommandWithoutArguments() {
        boolean canExecute = command.canExecute("!roll");
        assertFalse(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenRollCommandWithIncorrectSecondArgument() {
        boolean canExecute = command.canExecute("!roll qwe");
        assertFalse(canExecute);
    }

    @Test
    public void shouldReturnResponseYouLostWhenRollCommandRolledTen() {
        command.canExecute("!roll 10");
        when(random.nextInt(anyInt())).thenReturn(10);
        String execute = command.execute();
        assertEquals("POSITIV, you lost 10 points! (You have 990 points)", execute);
    }

    @Test
    public void shouldReturnResponseYouWonWhenRollCommandRolledHundred() {
        command.canExecute("!roll 30");
        when(random.nextInt(anyInt())).thenReturn(100);
        when(random.nextFloat()).thenReturn((float) 0.5);
        String execute = command.execute();
        assertEquals("POSITIV, you won 45 points! (You have 1045 points)", execute);
    }

    @Test
    public void shouldReturnResponseYouLostWhenRollCommandRolledSeventyFive() {
        command.canExecute("!roll 100");
        when(random.nextInt(anyInt())).thenReturn(75);
        String execute = command.execute();
        assertEquals("POSITIV, you lost 100 points! (You have 900 points)", execute);
    }

    @Test
    public void shouldReturnResponseYouWonWhenRollCommandRolledSeventySix() {
        command.canExecute("!roll 100");
        when(random.nextInt(anyInt())).thenReturn(76);
        when(random.nextFloat()).thenReturn((float) 0.5);
        String execute = command.execute();
        assertEquals("POSITIV, you won 150 points! (You have 1150 points)", execute);
    }

    @Test
    public void shouldReturnResponseNotEnoughPointsWhenRollCommandRolledMoreThanUserHave() {
        command.canExecute("!roll 2000");
        String execute = command.execute();
        assertEquals("POSITIV, you don't have enough points! (You have 1000 points)", execute);
    }

    @Test
    public void shouldReturnResponseEmptyWhenUserNotFound() {
        when(userRepository.getUserByName(anyString())).thenReturn(Optional.empty());
        String execute = command.execute();
        assertEquals("", execute);
    }
}
