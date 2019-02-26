package chat.unit.repository;

import chat.model.entity.Direct;
import chat.model.repository.DirectRepository;
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
public class DirectRepositoryTest {

    @Autowired
    private DirectRepository directRepository;

    @Test
    public void getAllDirectsTest() {
        final Set<Direct> directs = this.directRepository.getAll();
        assertTrue(!directs.isEmpty());
    }

    @Test
    public void getDirectByWordTest() {
        final Optional<Direct> directByWord = this.directRepository.getDirectByWord("POSITIV");
        assertTrue(directByWord.isPresent());
    }

    @Test
    public void getDirectByIncorrectWordTest() {
        final Optional<Direct> directByName = this.directRepository.getDirectByWord("QWE");
        assertTrue(!directByName.isPresent());
    }
}
