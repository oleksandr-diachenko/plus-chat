package chat.model.repository;

import chat.model.entity.Smile;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class JSONSmileRepository implements SmileRepository {

    private final static Logger logger = LogManager.getLogger(JSONSmileRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Smile> smiles;
    private String path;

    public JSONSmileRepository() {
    }

    public JSONSmileRepository(final String path) {
        this.path = path;
        this.smiles = getAll();
    }

    @Override
    public Set<Smile> getAll() {
        try {
            return new HashSet<>(
                    this.mapper.readValue(JSONParser.readFile(this.path), new TypeReference<List<Smile>>() {
            }));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Smile> getSmileByName(final String name) {
        for (Smile smile : this.smiles) {
            if (smile.getName().equalsIgnoreCase(name)) {
                return Optional.of(smile);
            }
        }
        return Optional.empty();
    }

    @Override
    public Smile add(final Smile smile) {
        this.smiles.add(smile);
        flush();
        return smile;
    }

    @Override
    public Smile update(final Smile smile) {
        this.smiles.remove(smile);
        this.smiles.add(smile);
        flush();
        return smile;
    }

    @Override
    public Smile delete(final Smile smile) {
        this.smiles.remove(smile);
        flush();
        return smile;
    }

    private void flush() {
        final Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(this.path), this.smiles);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Smiles failed to save. Create " +
                            this.path);
                }
            }
        });
        thread.start();
    }
}
