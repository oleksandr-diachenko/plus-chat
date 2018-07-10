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
public class Rank implements Comparable<Rank> {

    private String name;
    private int id;
    private int exp;
    private String imagePath;

    @Override
    public String toString() {
        return "Rank{" +
                "name='" + this.name + '\'' +
                ", id=" + this.id +
                ", exp=" + this.exp +
                '}';
    }

    @Override
    public int compareTo(Rank rank) {
        return Integer.compare(this.getId(), rank.getId());
    }
}
