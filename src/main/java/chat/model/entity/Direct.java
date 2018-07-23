package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class Direct {

    private String word;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Direct direct = (Direct) o;
        return Objects.equals(this.word, direct.getWord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.word);
    }

    @Override
    public String toString() {
        return "Direct{" +
                "word='" + this.word + '\'' +
                '}';
    }
}
