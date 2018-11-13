package chat.model.repository;

import chat.model.entity.Command;
import chat.util.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
@Repository
public class JSONCommandRepository implements CommandRepository {

    private final static Logger logger = LogManager.getLogger(JSONCommandRepository.class);

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Command> commands;
    private String path;

    public JSONCommandRepository() {
    }

    public JSONCommandRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Command> getAll() {
        try {
            this.commands = new HashSet<>(
                    this.mapper.readValue(JSONParser.readFile(this.path), new TypeReference<List<Command>>() {
                    }));
            return this.commands;
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new HashSet<>();
    }

    @Override
    public Optional<Command> getCommandByName(String name) {
        for (Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }

    @Override
    public Command add(Command command) {
        this.commands.add(command);
        flush();
        return command;
    }

    @Override
    public Command update(Command command) {
        this.commands.remove(command);
        this.commands.add(command);
        flush();
        return command;
    }

    @Override
    public Command delete(Command command) {
        this.commands.remove(command);
        flush();
        return command;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    this.mapper.writeValue(new FileOutputStream(this.path), this.commands);
                } catch (IOException exception) {
                    logger.error(exception.getMessage(), exception);
                    throw new RuntimeException("Commands failed to save. Create " +
                            this.path, exception);
                }
            }
        });
        thread.start();
    }
}
