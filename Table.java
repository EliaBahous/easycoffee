import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Table {
    private int tableNumber;
    private Boolean isOccupied;
    private DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private String connectionString = dbConnection.getConnectionString();

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(Boolean isOccupied) {
        this.isOccupied = isOccupied;
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

    public Table(int tableNumber, int isOccupied) {
        this.tableNumber = tableNumber;
        if (isOccupied == 0) {
            this.isOccupied = false;
        } else {
            this.isOccupied = true;
        }
    }

    public void addOrderItem(int tableNumber, OrderItem orderItem) {

        String updateSql = "UPDATE Tables SET is_occupied = ? WHERE table_number = ?";

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

    public void clearTable() {
        String deleteQuery = "DELETE FROM Tables WHERE table_number = ?;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            // Set values for the prepared statement
            stmt.setInt(1, this.tableNumber);
            // Execute the DELETE statement
            int rowsAffected = stmt.executeUpdate();

            // Check the number of rows affected (optional)
            if (rowsAffected > 0) {
                System.out.println("Table " + this.tableNumber + " is available now for orders.");
            } else {
                System.out.println("No table with number " + this.tableNumber + " found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
