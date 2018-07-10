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
                "name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", response='" + this.response + '\'' +
                '}';
    }
}
