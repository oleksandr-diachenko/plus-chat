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
    public void getAllCommandsTest() {
        final Set<Command> commands = this.commandRepository.getAll();
        assertTrue(!commands.isEmpty());
    }

    @Test
    public void getCommandByNameTest() {
        final Optional<Command> commandByName = this.commandRepository.getCommandByName("!info");
        assertTrue(commandByName.isPresent());
    }

    @Test
    public void getCommandByIncorrectNameTest() {
        final Optional<Command> commandByName = this.commandRepository.getCommandByName("!QWE");
        assertTrue(!commandByName.isPresent());
    }
}
