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
public class User implements Comparable<User>, Serializable {

    private String name;
    private String customName;
    private String firstMessageDate;
    private String lastMessageDate;
    private long exp;
    private long points;

    public String getCustomName() {
        if (customName != null) {
            return customName;
        }
        return name;
    }

    public void increaseExp() {
        exp++;
    }

    public void addPoints(long point) {
        points += point;
    }

    public void removePoints(long point) {
        points -= point;
    }

    @Override
    public int compareTo(User user) {
        return name.compareTo(user.getName());
    }
}
