package chat.model.repository;

import chat.model.entity.Rank;
import chat.util.JSONParser;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileOutputStream;
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
        this.ranks = getAll();
    }

    @Override
    public Set<Rank> getAll() {
        try {
            return new TreeSet<>(new HashSet<>(this.mapper.readValue(JSONParser.readFile(path), new TypeReference<List<Rank>>() {
            })));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Rank> getByName(final String name) {
        for (Rank rank : this.ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return Optional.of(rank);
            }
        }
        return Optional.empty();
    }

    @Override
    public Rank add(final Rank rank) {
        this.ranks.add(rank);
        flush();
        return rank;
    }

    @Override
    public Rank update(final Rank rank) {
        this.ranks.remove(rank);
        this.ranks.add(rank);
        flush();
        return rank;
    }

    @Override
    public Rank delete(final Rank rank) {
        this.ranks.remove(rank);
        flush();
        return rank;
    }

    @Override
    public Rank getRankByExp(final long exp) {
        Rank nearest = new Rank();
        for (Rank rank : this.ranks) {
            final int rankExp = rank.getExp();
            if (rankExp <= exp) {
                nearest = rank;
            }
        }
        return nearest;
    }

    private void flush() {
        final Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(path), this.ranks);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                }
            }
        });
        thread.start();
    }
}
