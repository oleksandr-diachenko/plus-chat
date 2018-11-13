package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class Smile {

    private String name;
    private String imagePath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Smile smile = (Smile) o;
        return Objects.equals(this.name, smile.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "Smile{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
