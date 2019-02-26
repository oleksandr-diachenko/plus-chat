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
public class Smile implements Comparable<Smile>, Serializable {

    private String name;
    private String imagePath;

    @Override
    public int compareTo(Smile smile) {
        return name.compareTo(smile.getName());
    }
}
