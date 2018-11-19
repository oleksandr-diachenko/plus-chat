package chat.unit.command;

import chat.command.ICommand;
import chat.command.OrderCommand;
import chat.model.repository.OrderRepository;
import chat.model.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OrderCommandTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    private ICommand command = new OrderCommand(userRepository, "p0sltlv", orderRepository);

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
    public void execute() {
    }
}