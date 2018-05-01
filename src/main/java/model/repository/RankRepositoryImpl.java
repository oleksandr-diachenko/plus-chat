package model.repository;

import model.entity.Rank;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import util.JSONParser;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class RankRepositoryImpl implements RankRepository {

    private ObjectMapper mapper = new ObjectMapper();

    private Set<Rank> ranks;

    public RankRepositoryImpl() {
        ranks = getRanks();
    }

    @Override
    public Set<Rank> getRanks() {
        try {
           return new TreeSet<>(new HashSet<>(mapper.readValue(JSONParser.readFile("./settings/ranks.json"), new TypeReference<List<Rank>>() {
            })));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TreeSet<>();
    }

    @Override
    public Rank getRankByName(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public Rank getRankById(int id) {
        for (Rank rank : ranks) {
            if (rank.getId() == id) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public Rank getTopRank() {
        Rank topRank = new Rank();
        for (Rank rank : ranks) {
            if (rank.getId() > topRank.getId()) {
                topRank = rank;
            }
        }
        return topRank;
    }

    @Override
    public Rank getNext(Rank current) {
        for (Rank rank : ranks) {
            if (rank.getId() - 1 == current.getId()) {
                return rank;
            }
        }
        return current;
    }

    @Override
    public Rank getRankByExp(int exp) {
        return null;
    }
}
