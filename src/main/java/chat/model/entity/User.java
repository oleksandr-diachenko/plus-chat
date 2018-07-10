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
public class User {

    private String name;
    private String firstMessageDate;
    private String lastMessageDate;
    private int exp;

    @Override
    public String toString() {
        return "User{" +
                "name='" + this.name + '\'' +
                ", firstMessageDate=" + this.firstMessageDate +
                ", lastMessageDate=" + this.lastMessageDate +
                ", exp=" + this.exp +
                '}';
    }
}
