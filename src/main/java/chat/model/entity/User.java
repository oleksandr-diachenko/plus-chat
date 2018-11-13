package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class User {

    private String name;
    private String customName;
    private String firstMessageDate;
    private String lastMessageDate;
    private long exp;
    private long points;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getCustomName() {
        if (customName != null) {
            return customName;
        }
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", customName='" + customName + '\'' +
                ", firstMessageDate='" + firstMessageDate + '\'' +
                ", lastMessageDate='" + lastMessageDate + '\'' +
                ", exp=" + exp +
                ", points=" + points +
                '}';
    }
}
