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

    public JSONGameRepository(final String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Game> getAll() {
        try {
            this.games = new HashSet<>(
                    this.mapper.readValue(JSONParser.readFile(this.path), new TypeReference<List<Game>>() {
                    }));
            return this.games;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Optional<Game> getGameByName(final String name) {
        for (Game game : this.games) {
            if (game.getName().equalsIgnoreCase(name)) {
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    @Override
    public Game add(final Game game) {
        this.games.add(game);
        flush();
        return game;
    }

    @Override
    public Game update(final Game game) {
        this.games.remove(game);
        this.games.add(game);
        flush();
        return game;
    }

    @Override
    public Game delete(final Game game) {
        this.games.remove(game);
        flush();
        return game;
    }

    private void flush() {
        final Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(this.path), this.games);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Games failed to save. Create " +
                            this.path, exception);
                }
            }
        });
        thread.start();
    }
}
