package chat.model.repository;

import chat.model.entity.Game;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Repository
public class JSONGameRepository implements GameRepository {

    private final static Logger logger = LogManager.getLogger(JSONGameRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Game> games;
    private String path;

    public JSONGameRepository() {
    }

    public JSONGameRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Game> getAll() {
        try {
            games = new HashSet<>(
                    mapper.readValue(JSONParser.readFile(path), new TypeReference<List<Game>>() {
                    }));
            return games;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Game> getGameByName(String name) {
        for (Game game : games) {
            if (game.getName().equalsIgnoreCase(name)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public Game add(Game game) {
        games.add(game);
        flush();
        return game;
    }

    @Override
    public Game update(Game game) {
        games.remove(game);
        games.add(game);
        flush();
        return game;
    }

    @Override
    public Game delete(Game game) {
        games.remove(game);
        flush();
        return game;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), games);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Games failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
