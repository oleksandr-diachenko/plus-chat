package chat.unit.repository;

import chat.model.entity.Direct;
import chat.model.repository.DirectRepository;
import chat.model.repository.JSONDirectRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
public class DirectRepositoryTest {

    private DirectRepository directRepository = new JSONDirectRepository(getResource("/json/directs.json"));

    @Test
    public void getAllDirectsTest() {
        final Set<Direct> directs = this.directRepository.getAll();
        assertTrue(!directs.isEmpty());
    }

    @Test
    public void getDirectByNameTest() {
        final Optional<Direct> directByName = this.directRepository.getDirectByWord("POSITIV");
        assertTrue(directByName.isPresent());
    }

    @Test
    public void getDirectByIncorrectNameTest() {
        final Optional<Direct> directByName = this.directRepository.getDirectByWord("QWE");
        assertTrue(!directByName.isPresent());
    }

    private String getResource(final String path) {
        return getClass().getResource(path).getPath();
    }
}
