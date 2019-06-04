package chat.unit.repository;

import chat.model.entity.Command;
import chat.model.repository.CommandRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class CommandRepositoryTest {

    @Autowired
    private CommandRepository commandRepository;

    @Test
    public void shouldReturnAllJsons() {
        Set<Command> commands = commandRepository.getAll();
        assertFalse(commands.isEmpty());
    }

    @Test
    public void shouldReturnJsonWhenCommandNameCorrect() {
        Optional<Command> commandByName = commandRepository.getCommandByName("!info");
        assertTrue(commandByName.isPresent());
    }

    @Test
    public void shouldNotReturnJsonWhenCommandNameIncorrect() {
        Optional<Command> commandByName = commandRepository.getCommandByName("!QWE");
        assertFalse(commandByName.isPresent());
    }
}
