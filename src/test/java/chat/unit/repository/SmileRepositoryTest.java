package chat.unit.repository;

import chat.model.entity.Smile;
import chat.model.repository.JSONSmileRepository;
import chat.model.repository.SmileRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
public class SmileRepositoryTest {

    private SmileRepository smileRepository = new JSONSmileRepository(getResource("/json/smiles.json"));

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

    private String getResource(final String path) {
        return getClass().getResource(path).getPath();
    }
}
