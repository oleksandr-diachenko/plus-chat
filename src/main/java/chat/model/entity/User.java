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

    public boolean hasCustomName(){
        return customName != null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(this.name, user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + this.name + '\'' +
                ", customName='" + this.customName + '\'' +
                ", firstMessageDate='" + this.firstMessageDate + '\'' +
                ", lastMessageDate='" + this.lastMessageDate + '\'' +
                ", exp=" + this.exp +
                '}';
    }
}
