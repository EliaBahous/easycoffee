import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public class OrderItem {
    MenuItem menuItem;
    int quantity;
    int tableNumber;
    LocalDateTime orderDate;
    LocalDateTime deliveryDate;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();

    public OrderItem(MenuItem menuItem, int quantity,int tableNumber) {
        this.menuItem = new MenuItem(menuItem.itemId,menuItem.itemName, menuItem.price, menuItem.quantityInStock, menuItem.totalSold,menuItem.discount);
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
        this.tableNumber = tableNumber;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void insertOrder(){
      
        String insertSQL = "INSERT INTO [dbo].[Orders]\r\n" + //
                        "           ([menu_item_id]\r\n" + //
                        "           ,[quantity]\r\n" + //
                        "           ,[order_date]\r\n" + //
                        "           ,[delivery_date]\r\n" + //
                        "           ,[table_number])\r\n" + //
                        "     VALUES(?,?,?,?,?)";

              try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {

               LocalDateTime localDateTime = LocalDateTime.now();

        // Convert LocalDateTime to LocalDate
        LocalDate localDate = localDateTime.toLocalDate();

        // Convert LocalDate to java.sql.Date
        Date sqlDate = Date.valueOf(localDate);
            System.err.println("menuitem");
            System.out.println(menuItem.itemId);
            insertStmt.setInt(1, menuItem.itemId);
            insertStmt.setDouble(2, quantity);
            insertStmt.setDate(3, sqlDate);
            insertStmt.setDate(4, null);
            insertStmt.setInt(5, tableNumber);

            // Execute the update query
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Could not update order for table with number: " + tableNumber);
            } else {
                System.out.println("Order Added successfully ,Table number: " + tableNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
