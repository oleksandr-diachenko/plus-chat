package chat.unit.command;

import chat.command.ICommand;
import chat.command.OrderCommand;
import chat.model.repository.OrderRepository;
import chat.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr_Diachenko
 */
public class OrderCommandTest {

    private ICommand command;

    @Before
    public void setup() {
        UserRepository userRepository = mock(UserRepository.class);
        OrderRepository orderRepository = mock(OrderRepository.class);
        String userName = "p0sltlv";
        when(userRepository.getUserByName(userName)).thenReturn(UserFactory.createUser());
        command = new OrderCommand(userRepository, userName, orderRepository);
    }

    @Test
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!order 100 order");
        assertTrue(canExecute);
    }

    @Test
    public void canExecute_wrongCommandTest() {
        boolean canExecute = command.canExecute("!qwe 100 order");
        assertFalse(canExecute);
    }

    @Test
    public void canExecute_wrongArgumentCount_oneArgumentTest() {
        boolean canExecute = command.canExecute("!order");
        assertFalse(canExecute);
    }

    @Test
    public void canExecute_wrongArgumentCount_twoArgumentTest() {
        boolean canExecute = command.canExecute("!order 100");
        assertFalse(canExecute);
    }

    @Test
    public void canExecute_wrongArgument_secondArgumentNotNumericTest() {
        boolean canExecute = command.canExecute("!order qwe order");
        assertFalse(canExecute);
    }

    @Test
    public void canExecute_fourArgumentTest() {
        boolean canExecute = command.canExecute("!order 100 order qwe");
        assertTrue(canExecute);
    }

    @Test
    public void executeTest() {
        command.canExecute("!order 100 order");
        String execute = command.execute();
        assertEquals("POSITIV, your order is accepted (order) (100 points)", execute);
    }

    @Test
    public void execute_notEnoughPointsTest() {
        command.canExecute("!order 10000 order");
        String execute = command.execute();
        assertEquals("POSITIV, you don't have enough points! (You have 1000 points)", execute);
    }
}
