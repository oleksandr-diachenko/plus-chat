package chat.model.repository;

import chat.model.entity.Command;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import chat.util.JSONParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class JSONCommandRepository implements CommandRepository {

    private final static Logger logger = Logger.getLogger(JSONCommandRepository.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<Command> getCommands() {
        try {
            return new HashSet<>(this.mapper.readValue(JSONParser.readFile("./data/commands.json"), new TypeReference<List<Command>>() {
            }));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public Optional<Command> getCommandByName(final String name) {
        for (Command command : getCommands()) {
            if (command.getName().equalsIgnoreCase(name)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }
}
