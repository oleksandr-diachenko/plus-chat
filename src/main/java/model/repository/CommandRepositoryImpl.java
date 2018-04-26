package model.repository;

import model.entity.Command;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import util.JSONParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public class CommandRepositoryImpl implements CommandRepository {

    private ObjectMapper mapper = new ObjectMapper();

    private Set<Command> commands;


    public CommandRepositoryImpl() {
        commands = getCommandList();
    }

    @Override
    public Set<Command> getCommands() {
        return commands;
    }

    @Override
    public Command getCommandByName(String name) {
        for (Command command: commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    @Override
    public void add(Command command) {
        commands.add(command);
        write();
    }

    @Override
    public void update(Command command) {
        commands.remove(command);
        commands.add(command);
        write();
    }

    @Override
    public void delete(Command command) {
        commands.remove(command);
        write();
    }

    private void write() {
        try {
            mapper.writeValue(new FileOutputStream("./commands.json"), commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
        commands = getCommandList();
    }

    private Set<Command> getCommandList() {
        try {
            return new HashSet<>(mapper.readValue(JSONParser.readFile("./commands.json"), new TypeReference<List<Command>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}
