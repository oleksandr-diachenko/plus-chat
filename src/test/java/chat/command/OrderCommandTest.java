package chat.command;

import chat.factory.AbstractFactory;
import chat.factory.UserFactory;
import chat.model.entity.User;
import chat.model.repository.OrderRepository;
import chat.model.repository.UserRepository;
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
public class OrderCommandTest {

    @InjectMocks
    private OrderCommand command;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup() {
        AbstractFactory<User> userFactory = new UserFactory();
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        command.setNick("p0sltlv");
    }

    @Test
    public void shouldExecuteWhenOrderCommandCorrect() {
        boolean canExecute = command.canExecute("!order 100 order");
        assertTrue(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenOrderCommandIncorrect() {
        boolean canExecute = command.canExecute("!qwe 100 order");
        assertFalse(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenOrderCommandWithoutArguments() {
        boolean canExecute = command.canExecute("!order");
        assertFalse(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenOrderCommandWithIncorrectArgumentsCount() {
        boolean canExecute = command.canExecute("!order 100");
        assertFalse(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenOrderCommandWithIncorrectSecondArgument() {
        boolean canExecute = command.canExecute("!order qwe order");
        assertFalse(canExecute);
    }

    @Test
    public void shouldExecuteWhenOrderCommandMoreThanThreeArguments() {
        boolean canExecute = command.canExecute("!order 100 order qwe");
        assertTrue(canExecute);
    }

    @Test
    public void shouldReturnResponseWhenOrderCommandCorrect() {
        command.canExecute("!order 100 order");
        String execute = command.execute();
        assertEquals("POSITIV, your order is accepted (order) (100 points)", execute);
    }

    @Test
    public void shouldReturnResponseNotEnoughPointsWhenUserDontHaveEnoughPoints() {
        command.canExecute("!order 10000 order");
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
