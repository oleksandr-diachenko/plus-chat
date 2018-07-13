package chat.model.repository;

import chat.model.entity.Rank;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import chat.util.JSONParser;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class JSONRankRepository implements RankRepository {

    private final static Logger logger = Logger.getLogger(JSONRankRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Rank> ranks;
    private String path;

    public JSONRankRepository(final String path) {
        this.path = path;
        this.ranks = getRanks();
    }

    @Override
    public Set<Rank> getRanks() {
        try {
            return new TreeSet<>(new HashSet<>(this.mapper.readValue(JSONParser.readFile(path), new TypeReference<List<Rank>>() {
            })));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Rank getRankByExp(final int exp) {
        Rank nearest = new Rank();
        for (Rank rank : this.ranks) {
            final int rankExp = rank.getExp();
            if (rankExp <= exp) {
                nearest = rank;
            }
        }
        return nearest;
    }
}
