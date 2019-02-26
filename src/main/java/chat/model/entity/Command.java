package chat.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "name")
@AllArgsConstructor
@NoArgsConstructor
public class Command implements Comparable<Command>, Serializable {

    private String name;
    private String description;
    private String response;

    @Override
    public int compareTo(Command command) {
        return name.compareTo(command.getName());
    }
}
