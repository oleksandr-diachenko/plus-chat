package model.repository;

import model.entity.Command;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import util.JSONParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class CommandRepositoryImpl implements CommandRepository {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<Command> getCommands() {
        try {
            return new HashSet<>(mapper.readValue(JSONParser.readFile("./settings/commands.json"), new TypeReference<List<Command>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}
