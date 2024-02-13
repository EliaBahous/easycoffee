import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class CafeSystem {
    final double LOW_STOCK_THRESHOLD = 0.2;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();

    public CafeSystem() { 
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

        if (table != null && !table.isOccupied && menuItem != null && menuItem.quantityInStock >= quantity) {
            OrderItem orderItem = new OrderItem(menuItem.itemId, quantity,tableNumber);
            table.addOrderItem(tableNumber,orderItem);
            menuItem.quantityInStock -= quantity;
            menuItem.totalSold += quantity;
            //update quantity for menu 
            menuItem.updateMenuItem(menuItem);
            //insert order to orders table 
            orderItem.insertOrder();
            System.out.println("Order placed successfully.");
        } else if (menuItem != null && menuItem.quantityInStock < quantity) {
            System.out.println("Insufficient stock for item: " + itemName);
        } else if(table.isOccupied){
            System.out.println("Table is already Occupied.");
        }
          else {
            System.out.println("Invalid table number or menu item.");
        }
    }

    public void cancelOrder(int tableNumber) {
        Table table = findTable(tableNumber);
        if(table == null){
          System.out.println("Invalid table number or Table not found.");
        }else {
            String deleteQuery = "DELETE FROM [dbo].[Orders] WHERE [table_number] = ?;";
            try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement stmt = connection.prepareStatement(deleteQuery)){
                       // Set values for the prepared statement
            stmt.setInt(1, tableNumber);
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
               System.out.println("All Orders for tableNumber" + tableNumber + "Where closed successfully");
            }else 
             System.out.println("No Orders found for tablenumber:" + tableNumber);
            }catch(SQLException e){
                e.printStackTrace();
            }  
        }
        table.clearTable(); 
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

    public void checkLowStock() {
      /*   for (MenuItem menuItem : menu) {
            if (!lowStockItems.contains(menuItem.itemName) && menuItem.quantityInStock < LOW_STOCK_THRESHOLD * menu.size()) {
                lowStockItems.add(menuItem.itemName);
                System.out.println("Low stock alert: " + menuItem.itemName);
            }
        }*/
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

    public void recordPayment(int tableNumber) {
        String updateQuery = "UPDATE [dbo].[Orders] SET [delivery_date] = ? WHERE table_number = ?";

        try(Connection connection = DriverManager.getConnection(connectionString);
        PreparedStatement stmt = connection.prepareStatement(updateQuery)){
         
            LocalDateTime deliveryDateTime = LocalDateTime.now();
            Date sqlDate = Date.valueOf(deliveryDateTime.toLocalDate());
            
            stmt.setDate(1, sqlDate);
            stmt.setInt(2, tableNumber);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Order is paied for table" + tableNumber);
                System.out.println("Payment recorded at: " + deliveryDateTime);
            }else{
                System.out.println("Could'nt find orders for table" + tableNumber);
            }
        }catch(SQLException e){
          e.printStackTrace();
        }
    }

    private Table findTable(int tableNumber) {
        String query = "SELECT * FROM [dbo].[Tables] where table_number =? ;";
        Table table = null;
        try(Connection connection = DriverManager.getConnection(connectionString);
        PreparedStatement stmt = connection.prepareStatement(query)){
                   // Set values for the prepared statement
        stmt.setInt(1, tableNumber);
        
        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        table = new Table(tableNumber,resultSet.getInt(3));
        }catch(SQLException e){
            e.printStackTrace();
        }  
        return table;
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
            int itemId = resultSet.getInt("item_id");

            menuItem = new MenuItem(itemId,name, price, quantityInStock, totalSold, discount);
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