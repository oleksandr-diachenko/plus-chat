package model.entity;

import java.util.Objects;

/**
 * @author Alexander Diachenko.
 */
public class Rank implements Comparable<Rank> {

    private String name;
    private int id;
    private int exp;
    private String imagePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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
