import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MenuItem {
    String itemName;
    double price;
    double quantityInStock;
    int totalSold;
    double discount;
    int itemId;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();
    public MenuItem(String itemName, double price, double quantityInStock) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = 0;
        this.discount = 0; //intially no discount
    }

    public MenuItem(String itemName, double price, double quantityInStock, int total_sold,double discount) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = total_sold;
        this.discount = discount; //intially no discount
    }

    // when placing an order we have menu item id 
    public MenuItem(int id,String itemName, double price, double quantityInStock, int total_sold,double discount) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = total_sold;
        this.discount = discount; //intially no discount
        this.itemId = id;
    }
       // Getter and setter methods for discount
    /*    public double getDiscount() {
        return discount;
    } */

    public void setDiscount(String itemName,double discount) {
        String updateSql = "UPDATE [dbo].[MenuItems] SET [discount] = ? WHERE [item_name] = ?";
        this.discount = discount;
              try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Set the discount and item name parameters in the prepared statement
            updateStmt.setDouble(1, discount);
            updateStmt.setString(2, itemName);

            // Execute the update query
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No menu item found with name: " + itemName);
            } else {
                System.out.println("Discount updated successfully for menu item: " + itemName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to apply discount to price
    public void applyDiscount() {
        double discountedPrice = price * (1 - discount / 100);
        System.out.println("Discounted price for " + itemName + ": $" + discountedPrice);
    }

    public void updateMenuItem(MenuItem menu){
      //update DB with new quantities 
      String updateSql = "UPDATE [dbo].[MenuItems] SET [quantity_in_stock] = ?,[total_sold] = ? WHERE [item_name] = ?";
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
}
