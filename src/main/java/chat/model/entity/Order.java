package chat.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"finishedDate", "status"})
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Comparable<Order>, Serializable {

    private String user;
    private long points;
    private String order;
    private String takenDate;
    private String finishedDate;
    private OrderStatus status;

    @Override
    public int compareTo(Order order) {
        return user.compareTo(order.getUser());
    }
}