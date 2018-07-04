package chat.model.entity;

import java.util.Objects;

/**
 * @author Alexander Diachenko.
 */
public class User {

    private String name;
    private String firstMessageDate;
    private String lastMessageDate;
    private int exp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstMessageDate() {
        return firstMessageDate;
    }

    public void setFirstMessageDate(String firstMessageDate) {
        this.firstMessageDate = firstMessageDate;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", firstMessageDate=" + firstMessageDate +
                ", lastMessageDate=" + lastMessageDate +
                ", exp=" + exp +
                '}';
    }
}
