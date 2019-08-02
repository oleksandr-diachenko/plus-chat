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
@EqualsAndHashCode(exclude = {"finishedDate", "status"})
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