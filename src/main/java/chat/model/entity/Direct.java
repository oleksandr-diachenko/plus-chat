package chat.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "word")
@AllArgsConstructor
@NoArgsConstructor
public class Direct implements Comparable<Direct>, Serializable {

    private String word;

    @Override
    public int compareTo(Direct direct) {
        return word.compareTo(direct.getWord());
    }
}
