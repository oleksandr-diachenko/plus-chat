package chat.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
@ToString(exclude = "imagePath")
@EqualsAndHashCode(of = "name")
@AllArgsConstructor
@NoArgsConstructor
public class Rank implements Comparable<Rank>, Serializable {

    private String name;
    private int id;
    private int exp;
    private String imagePath;

    @Override
    public int compareTo(final Rank rank) {
        return Integer.compare(id, rank.getId());
    }
}
