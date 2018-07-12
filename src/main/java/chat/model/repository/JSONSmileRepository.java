package chat.model.repository;

import chat.model.entity.Smile;
import chat.util.JSONParser;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class JSONSmileRepository implements SmileRepository {

    private final static Logger logger = Logger.getLogger(JSONSmileRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Smile> smiles;

    public JSONSmileRepository() {
        this.smiles = getSmiles();
    }

    @Override
    public Set<Smile> getSmiles() {
        try {
            return new HashSet<>(this.mapper.readValue(JSONParser.readFile("./data/smiles.json"), new TypeReference<List<Smile>>() {
            }));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
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
}
