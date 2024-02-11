import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    int tableNumber;
    Boolean isOccupied;
    List<OrderItem> orderItems;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();

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

    public void addOrderItem(int tableNumber,OrderItem orderItem) {
        MenuItem menu = orderItem.menuItem;
        orderItems.add(orderItem);
       
        String updateSql = "UPDATE [dbo].[Tables] SET [is_occupied] = ? WHERE [table_number] = ?";

              try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Set the discount and item name parameters in the prepared statement
            updateStmt.setDouble(1, 1);
            updateStmt.setInt(2, tableNumber);

            // Execute the update query
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No table found with number: " + tableNumber);
            } else {
                System.out.println("Updated successfully Table number: " + tableNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
