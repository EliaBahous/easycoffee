import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CafeSystem {
    List<Table> tables;
    List<MenuItem> menu;
    Set<String> lowStockItems;
    final double LOW_STOCK_THRESHOLD = 0.2;

    public CafeSystem() {
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.lowStockItems = new HashSet<>();
    }

    public void addTable(int tableNumber) {
        tables.add(new Table(tableNumber));
    }

    public void addMenuItem(String itemName, double price, double quantityInStock) {
        menu.add(new MenuItem(itemName, price, quantityInStock));
    }

    public void placeOrder(int tableNumber, String itemName, int quantity) {
        Table table = findTable(tableNumber);
        MenuItem menuItem = findMenuItem(itemName);

        if (table != null && menuItem != null && menuItem.quantityInStock >= quantity) {
            OrderItem orderItem = new OrderItem(menuItem, quantity);
            table.addOrderItem(orderItem);
            menuItem.quantityInStock -= quantity;
            menuItem.totalSold += quantity;
            System.out.println("Order placed successfully.");
        } else if (menuItem != null && menuItem.quantityInStock < quantity) {
            System.out.println("Insufficient stock for item: " + itemName);
        } else {
            System.out.println("Invalid table number or menu item.");
        }
    }

    public void closeOrder(int tableNumber) {
        Table table = findTable(tableNumber);

        if (table != null) {
            List<OrderItem> orderItems = table.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.menuItem.quantityInStock += orderItem.quantity;
            }
            table.getOrderItems().clear();
            System.out.println("Order closed successfully.");
        } else {
            System.out.println("Invalid table number.");
        }
    }

    public void changeOrder(int tableNumber, String itemName, int quantity) {
        Table table = findTable(tableNumber);
        MenuItem menuItem = findMenuItem(itemName);

        if (table != null && menuItem != null && menuItem.quantityInStock + quantity >= 0) {
            OrderItem existingOrderItem = null;
            for (OrderItem orderItem : table.getOrderItems()) {
                if (orderItem.menuItem == menuItem) {
                    existingOrderItem = orderItem;
                    break;
                }
            }
            if (existingOrderItem != null) {
                existingOrderItem.quantity += quantity;
                menuItem.quantityInStock -= quantity;
                menuItem.totalSold += quantity;
                System.out.println("Order changed successfully.");
            } else {
                System.out.println("Item not found in order.");
            }
        } else {
            System.out.println("Invalid table number or menu item, or insufficient stock.");
        }
    }

    public void displayOrder(int tableNumber) {
        Table table = findTable(tableNumber);

        if (table != null) {
            System.out.println("Table " + tableNumber + " Order:");
            List<OrderItem> orderItems = table.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                System.out.println(orderItem.menuItem.itemName + " - Quantity: " + orderItem.quantity + " - Ordered on: " + orderItem.orderDate + " - Delivered on: " + orderItem.deliveryDate);
            }
        } else {
            System.out.println("Invalid table number.");
        }
    }

    public void checkLowStock() {
        for (MenuItem menuItem : menu) {
            if (!lowStockItems.contains(menuItem.itemName) && menuItem.quantityInStock < LOW_STOCK_THRESHOLD * menu.size()) {
                lowStockItems.add(menuItem.itemName);
                System.out.println("Low stock alert: " + menuItem.itemName);
            }
        }
    }

    public void generateMonthlySalesReport() {
        System.out.println("Monthly Sales Report:");
        for (MenuItem menuItem : menu) {
            System.out.println(menuItem.itemName + " - Total Sold: " + menuItem.totalSold);
        }
    }

    public void generateMonthlySalesGraph() {
        System.out.println("Monthly Sales Graph:");
        for (MenuItem menuItem : menu) {
            System.out.print(menuItem.itemName + ": ");
            for (int i = 0; i < menuItem.totalSold; i++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public void recordPayment() {
        System.out.println("Payment recorded at: " + LocalDateTime.now());
    }

    private Table findTable(int tableNumber) {
        for (Table table : tables) {
            if (table.tableNumber == tableNumber) {
                return table;
            }
        }
        return null;
    }

    private MenuItem findMenuItem(String itemName) {
        for (MenuItem menuItem : menu) {
            if (menuItem.itemName.equals(itemName)) {
                return menuItem;
            }
        }
        return null;
    }
}