package chat.model.repository;

import chat.model.entity.Rank;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
@Repository
public class JSONRankRepository implements RankRepository {

    private final static Logger logger = LogManager.getLogger(JSONRankRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Rank> ranks;
    private String path;

    public JSONRankRepository() {
    }

    public JSONRankRepository(final String path) {
        this.path = path;
        this.ranks = getAll();
    }

    @Override
    public Set<Rank> getAll() {
        try {
            return new TreeSet<>(new HashSet<>(this.mapper.readValue(JSONParser.readFile(this.path), new TypeReference<List<Rank>>() {
            })));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
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
        return this.ranks
                .stream()
                .filter(rank -> rank.getExp() <= exp)
                .min(Comparator.reverseOrder())
                .orElse(new Rank());
    }

    private void flush() {
        final Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(this.path), this.ranks);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Ranks failed to save. Put ranks.json to data/");
                }
            }
        });
        thread.start();
    }
}
