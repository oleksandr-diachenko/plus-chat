package chat.model.repository;

import chat.model.entity.Direct;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class JSONDirectRepository implements DirectRepository {

    private final static Logger logger = LogManager.getLogger(JSONDirectRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Direct> directs;
    private String path;

    public JSONDirectRepository(final String path) {
        this.path = path;
        this.directs = getAll();
    }

    @Override
    public Set<Direct> getAll() {
        try {
            return new HashSet<>(this.mapper.readValue(JSONParser.readFile(path), new TypeReference<List<Direct>>() {
            }));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Direct> getDirectByWord(final String word) {
        return this.directs
                .stream()
                .filter(command -> command.getWord().equalsIgnoreCase(word))
                .findFirst();
    }

    @Override
    public Direct add(final Direct direct) {
        this.directs.add(direct);
        flush();
        return direct;
    }

    @Override
    public Direct update(final Direct direct) {
        this.directs.remove(direct);
        this.directs.add(direct);
        flush();
        return direct;
    }

    @Override
    public Direct delete(final Direct direct) {
        this.directs.remove(direct);
        flush();
        return direct;
    }

    private void flush() {
        final Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(path), this.directs);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Directs failed to save. Put directs.json to data/");
                }
            }
        });
        thread.start();
    }
}
