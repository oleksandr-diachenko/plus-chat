package chat.unit.repository;

import chat.model.entity.Smile;
import chat.model.repository.SmileRepository;
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
public class SmileRepositoryTest {

    @Autowired
    private SmileRepository smileRepository;

    @Test
    public void getAllSmilesTest() {
        final Set<Smile> smiles = this.smileRepository.getAll();
        assertTrue(!smiles.isEmpty());
    }

    @Test
    public void getSmileByNameTest() {
        final Optional<Smile> smileByName = this.smileRepository.getSmileByName(":)");
        assertTrue(smileByName.isPresent());
    }

    @Test
    public void getSmileByIncorrectNameTest() {
        final Optional<Smile> smileByName = this.smileRepository.getSmileByName("QWE");
        assertTrue(!smileByName.isPresent());
    }
}
