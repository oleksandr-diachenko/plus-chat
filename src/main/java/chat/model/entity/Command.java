package chat.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Command {

    private String name;
    private String description;
    private String response;

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
