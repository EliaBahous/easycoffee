import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public class CafeSystem {
    List<Table> tables;
    List<MenuItem> menu;
    Set<String> lowStockItems;
    final double LOW_STOCK_THRESHOLD = 0.2;
    Connection connection = null;
    Statement statement = null;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();

    public CafeSystem() {
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.lowStockItems = new HashSet<>();  
    }

    public void addTable(int tableNumber) {
        String query = "INSERT INTO [dbo].[Tables] ([table_number], [is_occupied]) VALUES (?, ?)";
        String selectSql = "SELECT COUNT(*) AS tableExists FROM [dbo].[Tables] WHERE [table_number] = ?";

        int isOccupied = 0; // default for new table is 0
        try (Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement stmt = connection.prepareStatement(query);
            PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            
            // check if table exists 
            selectStmt.setInt(1,tableNumber);
            ResultSet resultSet = selectStmt.executeQuery();
            resultSet.next();
            int tableExists = resultSet.getInt("tableExists");
    

            if(tableExists > 0){
                System.out.println("Table already exists in the database. Skipping insertion.");

            }else {
           // Set values for the prepared statement
            stmt.setInt(1, tableNumber);
            stmt.setInt(2, isOccupied);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Table added successfully!");
            } else {
                System.out.println("Failed to add table.");
            }
           // add instance to Tables local list.
              tables.add(new Table(tableNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }

    public void addMenuItem(String itemName, double price, double quantityInStock) {
        String query = "INSERT INTO [dbo].[MenuItems] ([item_name], [price], [quantity_in_stock], [total_sold], [discount]) VALUES (?, ?, ?, ?, ?)";
        String selectSql = "SELECT COUNT(*) AS itemExists FROM [dbo].[MenuItems] WHERE [item_name] = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
        PreparedStatement stmt = connection.prepareStatement(query);
        PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {

        selectStmt.setString(1, itemName);
        ResultSet resultSet = selectStmt.executeQuery();
        resultSet.next();
        int itemExists = resultSet.getInt("itemExists");

        if (itemExists > 0) {
            System.out.println("Menu item already exists in the database. Skipping insertion.");
        }else{
       
        // Set values for the prepared statement
        stmt.setString(1, itemName);
        stmt.setDouble(2, price);
        stmt.setDouble(3, quantityInStock);
        //default values for total sold and discount
        stmt.setInt(4, 0); 
        stmt.setInt(5, 0); 

        // Execute the query
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            //update local list
            menu.add(new MenuItem(itemName, price, quantityInStock));
            System.out.println("MenuItem added successfully!");
        } else {
            System.out.println("Failed to add MEnuItem.");
        }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
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
        MenuItem menuItem = null;
        String query =  "SELECT * FROM [dbo].[MenuItems] WHERE [item_name] = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
        PreparedStatement stmt = connection.prepareStatement(query)) {

        // Set values for the prepared statement
        stmt.setString(1, itemName);
        
        ResultSet resultSet = stmt.executeQuery();

        // Check if a result was returned
        if (resultSet.next()) {
                // Retrieve data from the result set and create a MenuItem object
            String name = resultSet.getString("item_name");
            double price = resultSet.getDouble("price");
            int quantityInStock = resultSet.getInt("quantity_in_stock");
            int totalSold = resultSet.getInt("total_sold");
            double discount = resultSet.getDouble("discount");
                
            menuItem = new MenuItem(name, price, quantityInStock, totalSold, discount);
            }     
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return menuItem;
    }

    public void applyDiscount(String itemName, double discountPercentage) {
        MenuItem menuItem = findMenuItem(itemName);
        if (menuItem != null) {
            menuItem.setDiscount(itemName,discountPercentage);
            System.out.println("Discount applied to " + itemName);
        } else {
            System.out.println("Menu item not found.");
        }
    }
}