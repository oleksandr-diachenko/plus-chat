package model.entity;

import java.util.Date;
import java.util.Objects;

/**
 * @author Alexander Diachenko.
 */
public class User {

    private String name;
    private Date firstMessage;
    private Date lastMessage;
    private int level;
    private int exp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(Date firstMessage) {
        this.firstMessage = firstMessage;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
                ", firstMessage=" + firstMessage +
                ", lastMessage=" + lastMessage +
                ", level=" + level +
                ", exp=" + exp +
                '}';
    }
}
