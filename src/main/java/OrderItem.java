import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrderItem {
    private int menuItemId;
    private int quantity;
    private int tableNumber;
    private LocalDateTime orderDate;
    private DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private String connectionString = dbConnection.getConnectionString();
    private int paid;

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

    public OrderItem(int menuItemId, int quantity, int tableNumber) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
        this.tableNumber = tableNumber;
        this.paid = 0;
    }


    public void insertOrder() {

        String insertSQL = "INSERT INTO  Orders (order_id, menu_item_id, quantity, order_date, delivery_date, table_number, paid) VALUES(?,?,?,sysdate(),?,?,?)";

        try (Connection conn = DriverManager.getConnection(connectionString);
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {

            // Convert LocalDate to java.sql.Date
            insertStmt.setString(1, IDGenerator.generateID());
            insertStmt.setInt(2, menuItemId);
            insertStmt.setInt(3, quantity);
            insertStmt.setDate(4, null);
            insertStmt.setInt(5, tableNumber);
            insertStmt.setInt(6, paid);

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
