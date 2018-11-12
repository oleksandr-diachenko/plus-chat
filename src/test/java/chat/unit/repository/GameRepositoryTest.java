package chat.unit.repository;

import chat.model.entity.Game;
import chat.model.repository.GameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void getAllGamesTest() {
        final Set<Game> games = this.gameRepository.getAll();
        assertTrue(!games.isEmpty());
    }

    @Test
    public void getGameByNameTest() {
        final Optional<Game> gameByName = this.gameRepository.getGameByName("XCOM 2");
        assertTrue(gameByName.isPresent());
    }

    @Test
    public void getGameByIncorrectNameTest() {
        final Optional<Game> gameByName = this.gameRepository.getGameByName("QWE");
        assertTrue(!gameByName.isPresent());
    }
}
