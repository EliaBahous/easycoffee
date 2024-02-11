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
    String connectionUrl =
    "jdbc:sqlserver://metadatasqldb.database.windows.net:1433;"
            + "database=easycafe;"
            + "user=metadatasqldb;"
            + "password=Uh995512.;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";
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
       // Getter and setter methods for discount
       public double getDiscount() {
        return discount;
    }

    public void setDiscount(String itemName,double discount) {
        String updateSql = "UPDATE [dbo].[MenuItems] SET [discount] = ? WHERE [item_name] = ?";
        this.discount = discount;
              try (Connection conn = DriverManager.getConnection(connectionUrl);
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
}
