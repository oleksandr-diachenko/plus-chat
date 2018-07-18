package chat.model.repository;

import chat.model.entity.Direct;
import chat.util.JSONParser;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class JSONDirectRepository implements DirectRepository {

    private final static Logger logger = Logger.getLogger(JSONDirectRepository.class);

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
    public Optional<Direct> getByName(final String name) {
        for (Direct direct : this.directs) {
            if (direct.getWord().equalsIgnoreCase(name)) {
                return Optional.of(direct);
            }
        }
        return Optional.empty();
    }

    @Override
    public Direct add(final Direct direct) {
        this.directs.add(direct);
        return direct;
    }

    @Override
    public Direct update(final Direct direct) {
        this.directs.remove(direct);
        this.directs.add(direct);
        return direct;
    }

    @Override
    public Direct delete(final Direct direct) {
        this.directs.remove(direct);
        return direct;
    }
}
