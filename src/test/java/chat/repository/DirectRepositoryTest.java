package chat.repository;

import chat.model.entity.Direct;
import chat.model.repository.DirectRepository;
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
public class DirectRepositoryTest {

    @Autowired
    private DirectRepository directRepository;

    @Test
    public void shouldReturnAllDirects() {
        Set<Direct> directs = directRepository.getAll();
        assertFalse(directs.isEmpty());
    }

    @Test
    public void shouldReturnDirectWhenWordCorrect() {
        Optional<Direct> directByWord = directRepository.getDirectByWord("POSITIV");
        assertTrue(directByWord.isPresent());
    }

    @Test
    public void shouldNotReturnDirectWhenWordIncorrect() {
        Optional<Direct> directByName = directRepository.getDirectByWord("QWE");
        assertFalse(directByName.isPresent());
    }
}
