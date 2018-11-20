package chat.unit.command;

import chat.command.ICommand;
import chat.command.RankCommand;
import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;
import chat.unit.factory.AbstractFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr_Diachenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class RankCommandTest {

    private ICommand command;
    @Autowired
    private AbstractFactory<User> userFactory;
    @Autowired
    private AbstractFactory<Rank> rankFactory;


    @Before
    public void setup() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUserByName(anyString())).thenReturn(userFactory.create());
        RankRepository rankRepository = mock(RankRepository.class);
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