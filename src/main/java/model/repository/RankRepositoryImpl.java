package model.repository;

import model.entity.Rank;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import util.JSONParser;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class RankRepositoryImpl implements RankRepository {

    private final static Logger logger = Logger.getLogger(RankRepositoryImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Set<Rank> ranks;

    public RankRepositoryImpl() {
        ranks = getRanks();
    }

    @Override
    public Set<Rank> getRanks() {
        try {
            return new TreeSet<>(new HashSet<>(mapper.readValue(JSONParser.readFile("./data/ranks.json"), new TypeReference<List<Rank>>() {
            })));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Rank getRankByExp(int exp) {
        Rank nearest = new Rank();
        for (Rank rank : ranks) {
            int rankExp = rank.getExp();
            if (rankExp <= exp) {
                nearest = rank;
            }
        }
        return nearest;
    }
}
