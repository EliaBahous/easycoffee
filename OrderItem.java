import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public class OrderItem {
    private int menuItemId;
    private int quantity;
    private int tableNumber;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private String connectionString = dbConnection.getConnectionString();

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public DatabaseConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public OrderItem(int menuItemId, int quantity,int tableNumber) {
        this.menuItemId = menuItemId;
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
            insertStmt.setInt(1, menuItemId);
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
