package chat.command;

import chat.factory.AbstractFactory;
import chat.factory.RankFactory;
import chat.factory.UserFactory;
import chat.model.entity.Rank;
import chat.model.entity.User;
import chat.model.repository.RankRepository;
import chat.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

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
    public void shouldExecuteWhenRankCommandCorrect() {
        boolean canExecute = command.canExecute("!rank");
        assertTrue(canExecute);
    }

    @Test
    public void shouldNotExecuteWhenRankCommandIncorrect() {
        boolean canExecute = command.canExecute("!qwe");
        assertFalse(canExecute);
    }

    @Test
    public void shouldReturnResponseWhenRankCommandCorrect() {
        command.canExecute("!rank");
        String execute = command.execute();
        assertEquals("POSITIV, your rank Pro (10 exp)", execute);
    }

    @Test
    public void shouldReturnResponseEmptyWhenUserNotFound() {
        when(userRepository.getUserByName(anyString())).thenReturn(Optional.empty());
        String execute = command.execute();
        assertEquals("", execute);
    }
}