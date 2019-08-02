package chat.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class Command implements Comparable<Command>, Serializable {

    private String name;
    private String description;
    private String response;
    private Status status;

    @Override
    public int compareTo(Command command) {
        return name.compareTo(command.getName());
    }
}
