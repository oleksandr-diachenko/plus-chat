package chat.model.repository;

import chat.model.entity.Rank;
import chat.util.JSONParser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Alexander Diachenko.
 */
@Repository
@NoArgsConstructor
@Log4j2
public class JSONRankRepository implements RankRepository {

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Rank> ranks;
    private String path;

    public JSONRankRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Rank> getAll() {
        try {
            ranks = mapper.readValue(JSONParser.readFile(path), new TypeReference<TreeSet<Rank>>() {});
            return ranks;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Rank add(Rank rank) {
        ranks.add(rank);
        flush();
        return rank;
    }

    @Override
    public Rank update(Rank rank) {
        ranks.remove(rank);
        ranks.add(rank);
        flush();
        return rank;
    }

    @Override
    public Rank delete(Rank rank) {
        ranks.remove(rank);
        flush();
        return rank;
    }

    @Override
    public Rank getRankByExp(long exp) {
        Rank nearest = new Rank();
        for (Rank rank : ranks) {
            int rankExp = rank.getExp();
            if (rankExp <= exp) {
                nearest = rank;
            }
        }
        return nearest;
    }

    @Override
    public boolean isNewRank(long exp) {
        Rank rankByExp = getRankByExp(exp);
        return rankByExp.getExp() == exp;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), ranks);
                } catch (IOException exception) {
                    log.error(exception.getMessage(), exception);
                    throw new RuntimeException("Ranks failed to save. Create " + path, exception);
                }
            }
        });
        thread.start();
    }
}
