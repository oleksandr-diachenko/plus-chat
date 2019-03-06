package chat.model.repository;

import chat.model.entity.Smile;
import chat.util.FileUtil;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Repository
@NoArgsConstructor
@Log4j2
public class JSONSmileRepository implements SmileRepository {

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Smile> smiles;
    private String path;

    public JSONSmileRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Smile> getAll() {
        try {
            smiles = mapper.readValue(FileUtil.readFile(path), new TypeReference<Set<Smile>>() {});
            return smiles;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return new HashSet<>();
    }

    @Override
    public Optional<Smile> getSmileByName(String name) {
        for (Smile smile : smiles) {
            if (smile.getName().equalsIgnoreCase(name)) {
                return Optional.of(smile);
            }
        }
        return Optional.empty();
    }

    @Override
    public Smile add(Smile smile) {
        smiles.add(smile);
        flush();
        return smile;
    }

    @Override
    public Smile update(Smile smile) {
        smiles.remove(smile);
        smiles.add(smile);
        flush();
        return smile;
    }

    @Override
    public Smile delete(Smile smile) {
        smiles.remove(smile);
        flush();
        return smile;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), smiles);
                } catch (IOException exception) {
                    log.error(exception.getMessage(), exception);
                    throw new RuntimeException("Smiles failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
