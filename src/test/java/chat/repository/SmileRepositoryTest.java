package chat.repository;

import chat.model.entity.Smile;
import chat.model.repository.SmileRepository;
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
public class SmileRepositoryTest {

    @Autowired
    private SmileRepository smileRepository;

    @Test
    public void shouldReturnAllSmiles() {
        Set<Smile> smiles = smileRepository.getAll();
        assertFalse(smiles.isEmpty());
    }

    @Test
    public void shouldReturnSmileWhenSmileNameCorrect() {
        Optional<Smile> smileByName = smileRepository.getSmileByName(":)");
        assertTrue(smileByName.isPresent());
    }

    @Test
    public void shouldNotReturnSmileWhenSmileNameIncorrect() {
        Optional<Smile> smileByName = smileRepository.getSmileByName("QWE");
        assertFalse(smileByName.isPresent());
    }
}
