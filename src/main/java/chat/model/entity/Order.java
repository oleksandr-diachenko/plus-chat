package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class Order {

    private User user;
    private long points;
    private String order;
    private String takenDate;
    private String finishedDate;
    private OrderStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order1 = (Order) o;
        return points == order1.points &&
                Objects.equals(user, order1.user) &&
                Objects.equals(order, order1.order) &&
                Objects.equals(takenDate, order1.takenDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, points, order, takenDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user.getCustomName() +
                ", points=" + points +
                ", order='" + order + '\'' +
                ", takenDate='" + takenDate + '\'' +
                ", finishedDate='" + finishedDate + '\'' +
                ", status=" + status +
                '}';
    }
}
