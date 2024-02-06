import java.util.ArrayList;
import java.util.List;

public class Table {
    int tableNumber;
    List<OrderItem> orderItems;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.orderItems = new ArrayList<>();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
