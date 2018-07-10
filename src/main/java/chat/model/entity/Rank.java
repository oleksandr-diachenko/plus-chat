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
        return id == rank.id &&
                exp == rank.exp &&
                Objects.equals(name, rank.name) &&
                Objects.equals(imagePath, rank.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, exp, imagePath);
    }

    @Override
    public String toString() {
        return "Rank{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", exp=" + exp +
                '}';
    }

    @Override
    public int compareTo(Rank rank) {
        return Integer.compare(this.getId(), rank.getId());
    }
}
