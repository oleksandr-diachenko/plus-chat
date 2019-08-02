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
public class Smile implements Comparable<Smile>, Serializable {

    private String name;
    private String imagePath;

    @Override
    public int compareTo(Smile smile) {
        return name.compareTo(smile.getName());
    }
}
