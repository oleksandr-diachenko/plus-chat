package chat.model.repository;

import chat.model.entity.Direct;
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
public class JSONDirectRepository implements DirectRepository {

    private final static Logger logger = LogManager.getLogger(JSONDirectRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Direct> directs;
    private String path;

    public JSONDirectRepository() {
    }

    public JSONDirectRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Direct> getAll() {
        try {
            directs = new HashSet<>(
                    mapper.readValue(JSONParser.readFile(path), new TypeReference<List<Direct>>() {
                    }));
            return directs;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Direct> getDirectByWord(String word) {
        for (Direct direct : directs) {
            if(direct.getWord().equalsIgnoreCase(word)) {
                return Optional.of(direct);
            }
        }
        return Optional.empty();
    }

    @Override
    public Direct add(Direct direct) {
        directs.add(direct);
        flush();
        return direct;
    }

    @Override
    public Direct update(Direct direct) {
        directs.remove(direct);
        directs.add(direct);
        flush();
        return direct;
    }

    @Override
    public Direct delete(Direct direct) {
        directs.remove(direct);
        flush();
        return direct;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), directs);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Directs failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
