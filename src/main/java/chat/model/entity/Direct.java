package chat.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "word")
public class Direct implements Comparable<Direct>, Serializable {

    private String word;

    @Override
    public int compareTo(Direct direct) {
        return word.compareTo(direct.getWord());
    }
}
