package chat.model.entity;


import lombok.*;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imagePath")
@EqualsAndHashCode(of = "name")
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
