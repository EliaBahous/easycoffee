import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MenuItem {
    private String itemName;
    private double price;
    private double quantityInStock;
    private int totalSold;
    private double discount;
    private int itemId;
    private int expectedAmount; // New attribute to represent the expected amount
    private DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private String connectionString = dbConnection.getConnectionString();

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public MenuItem(String itemName, double price, double quantityInStock, int expectedAmount) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = 0;
        this.discount = 0; // intially no discount
        this.expectedAmount = expectedAmount;
    }

    public MenuItem(String itemName, double price, double quantityInStock, int total_sold, double discount,
            int expectedAmount) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = total_sold;
        this.discount = discount; // intially no discount
        this.expectedAmount = expectedAmount;
    }

    // when placing an order we have menu item id
    public MenuItem(int id, String itemName, double price, double quantityInStock, int total_sold, double discount) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = total_sold;
        this.discount = discount; // intially no discount
        this.itemId = id;
    }
    // Getter and setter methods for discount
    /*
     * public double getDiscount() {
     * return discount;
     * }
     */

    public boolean setDiscount(int itemId, double discount) {
        String updateSql = "UPDATE MenuItems SET discount = ? WHERE item_id = ?";
        this.discount = discount;
        try (Connection conn = DriverManager.getConnection(connectionString);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Set the discount and item name parameters in the prepared statement
            updateStmt.setDouble(1, discount);
            updateStmt.setInt(2, itemId);

            // Execute the update query
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No menu item found with name: " + itemId);
                return false;
            } else {
                System.out.println("Discount updated successfully for menu item: " + itemId + " Discount: " + discount);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to apply discount to price
    public void applyDiscount() {
        double discountedPrice = price * (1 - discount / 100);
        System.out.println("Discounted price for " + itemName + ": $" + discountedPrice);
    }

    public void updateMenuItem(MenuItem menu) {
        // update DB with new quantities
        String updateSql = "UPDATE MenuItems SET quantity_in_stock = ?,total_sold = ? WHERE item_name = ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Set the discount and item name parameters in the prepared statement
            updateStmt.setDouble(1, menu.quantityInStock);
            updateStmt.setInt(2, menu.totalSold);
            updateStmt.setString(3, menu.itemName);

            // Execute the update query
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No menu item found with name: " + itemName);
            } else {
                System.out.println("Quantity and total sold updated successfully for menu item: " + itemName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(int expectedAmount) {
        this.expectedAmount = expectedAmount;
    }
}
