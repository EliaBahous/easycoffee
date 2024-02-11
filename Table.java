import java.util.ArrayList;
import java.util.List;

public class Table {
    int tableNumber;
    Boolean isOccupied;
    List<OrderItem> orderItems;
    
    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        this.orderItems = new ArrayList<>();
    }

    public Table(int tableNumber,int isOccupied) {
        this.tableNumber = tableNumber;
        this.orderItems = new ArrayList<>();
        if(isOccupied == 0){
            this.isOccupied = false;
        }
        else {
            this.isOccupied = true;
        }      
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
