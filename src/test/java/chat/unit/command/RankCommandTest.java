package chat.unit.command;

import chat.command.ICommand;
import chat.command.RankCommand;
import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;
import chat.unit.factory.AbstractFactory;
import chat.unit.factory.RankFactory;
import chat.unit.factory.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(MockitoJUnitRunner.class)
public class RankCommandTest {

    private ICommand command;

    @Mock
    private RankRepository rankRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        AbstractFactory<User> userFactory = new UserFactory();
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        AbstractFactory<Rank> rankFactory = new RankFactory();
        when(rankRepository.getRankByExp(anyLong())).thenReturn(rankFactory.create().get());
        command = new RankCommand("p0sltlv", userRepository, rankRepository);
    }

    @Test
    public void canExecuteTest() {
        boolean canExecute = command.canExecute("!rank");
        assertTrue(canExecute);
    }

    @Test
    public void canExecute_wrongCommandTest() {
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void executeTest() {
        command.canExecute("!rank");
        String execute = command.execute();
        assertEquals("POSITIV, your rank Pro (10 exp)", execute);
    }
}