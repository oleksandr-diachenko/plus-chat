package chat.model.repository;

import chat.model.entity.Order;
import chat.util.JSONParser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

@Repository
@NoArgsConstructor
@Log4j2
public class JSONOrderRepository implements OrderRepository {

    private ObjectMapper mapper = new ObjectMapper();
    private Set<Order> orders;
    private String path;

    public JSONOrderRepository(String path) {
        this.path = path;
        getAll();
    }

    @Override
    public Set<Order> getAll() {
        try {
            orders = mapper.readValue(JSONParser.readFile(path), new TypeReference<Set<Order>>() {});
            return orders;
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return new TreeSet<>();
    }

    @Override
    public Order add(Order order) {
        orders.add(order);
        flush();
        return order;
    }

    @Override
    public Order update(Order order) {
        orders.remove(order);
        orders.add(order);
        flush();
        return order;
    }

    @Override
    public Order delete(Order order) {
        orders.remove(order);
        flush();
        return order;
    }

    private void flush() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                try {
                    mapper.writeValue(new FileOutputStream(path), orders);
                } catch (IOException exception) {
                    log.error(exception.getMessage(), exception);
                    throw new RuntimeException("Order failed to save. Create " +
                            path, exception);
                }
            }
        });
        thread.start();
    }
}
