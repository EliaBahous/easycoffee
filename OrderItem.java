import java.time.LocalDateTime;

public class OrderItem {
    MenuItem menuItem;
    int quantity;
    LocalDateTime orderDate;
    LocalDateTime deliveryDate;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
