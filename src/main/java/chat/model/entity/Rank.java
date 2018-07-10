package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class Rank implements Comparable<Rank> {

    private String name;
    private int id;
    private int exp;
    private String imagePath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return Objects.equals(name, rank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

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
