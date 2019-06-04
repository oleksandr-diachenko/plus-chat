package chat.command;

import chat.model.entity.Order;
import chat.model.entity.OrderStatus;
import chat.model.entity.User;
import chat.model.repository.OrderRepository;
import chat.model.repository.UserRepository;
import chat.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderCommand implements ICommand {

    private static final int MINIMAL_ARGUMENTS_LENGTH = 3;
    private static final int ORDER_INDEX_FROM = 2;
    private static final String COMMAND_NAME = "!order";
    private final UserRepository userRepository;
    private final String nick;
    private OrderRepository orderRepository;
    private long points;
    private String order;

    public OrderCommand(UserRepository userRepository, String nick,
                        OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.nick = nick;
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean canExecute(String command) {
        String[] arguments = command.split(" ");
        if (!correctArguments(arguments)) {
            return false;
        }
        points = Long.parseLong(arguments[1]);
        order = getOrder(arguments);
        return true;
    }

    private boolean correctArguments(String[] arguments) {
        return arguments.length >= MINIMAL_ARGUMENTS_LENGTH
                && COMMAND_NAME.equalsIgnoreCase(arguments[0])
                && StringUtils.isNumeric(arguments[1]);
    }

    private String getOrder(String[] parts) {
        StringBuilder builder = new StringBuilder();
        for (int index = ORDER_INDEX_FROM; index < parts.length; index++) {
            builder.append(parts[index]).append(" ");
        }
        return builder.toString().trim();
    }

    @Override
    public String execute() {
        Optional<User> userByName = userRepository.getUserByName(nick);
        if (userByName.isEmpty()) {
            return "";
        }
        User user = userByName.get();
        long userPoints = user.getPoints();
        if (notEnoughPoints(userPoints)) {
            return user.getCustomName() + ", you don't have enough points! (You have " + userPoints + " points)";
        }
        orderRepository.add(getOrder(user));
        user.setPoints(userPoints - points);
        userRepository.update(user);
        return user.getCustomName() + ", your order is accepted (" + order + ") (" + points + " points)";
    }

    private Order getOrder(User user) {
        Order order = new Order();
        order.setUser(user.getCustomName());
        order.setPoints(points);
        order.setOrder(this.order);
        order.setTakenDate(TimeUtil.formatDate(LocalDateTime.now()));
        order.setStatus(OrderStatus.NEW);
        return order;
    }

    private boolean notEnoughPoints(long userPoints) {
        return userPoints < points;
    }
}
