import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CafeSystem {
    final double LOW_STOCK_THRESHOLD = 0.2;
    DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    String connectionString = dbConnection.getConnectionString();

    public CafeSystem() {
    }

    public void addTable() {
        String query = "INSERT INTO Tables (table_id, table_number, is_occupied) VALUES (?, ?, ?)";
        String selectSql = "SELECT COUNT(*) AS tableExists FROM Tables";

        int isOccupied = 0; // default for new table is 0
        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(query);
             PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {

            // check if table exists
            ResultSet resultSet = selectStmt.executeQuery();
            resultSet.next();
            int tableExists = resultSet.getInt("tableExists");

            // Set values for the prepared statement
            stmt.setInt(1, tableExists+1);
            stmt.setInt(2, tableExists+1);
            stmt.setInt(3, isOccupied);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Table added successfully!");
            } else {
                System.out.println("Failed to add table.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int addMenuItem(int itemId, String itemName, double price, double quantityInStock, double discount) {
        String query = "INSERT INTO MenuItems (item_name, price, quantity_in_stock, total_sold, discount, expectedAmount, item_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String selectSql = "SELECT COUNT(*) AS itemExists FROM MenuItems WHERE item_id = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query);
                PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {

            selectStmt.setInt(1, itemId);
            ResultSet resultSet = selectStmt.executeQuery();
            resultSet.next();
            int itemExists = resultSet.getInt("itemExists");

            if (itemExists > 0) {
                return 0;
            } else {

                // Set values for the prepared statement
                stmt.setString(1, itemName);
                stmt.setDouble(2, price);
                stmt.setDouble(3, quantityInStock);
                // default values for total sold and discount
                stmt.setInt(4, 0);
                stmt.setDouble(5, discount);
                stmt.setInt(6, 0);
                stmt.setInt(7, itemId);
                // Execute the query
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public boolean placeOrder(int tableNumber, int itemId, int quantity) {
        Table table = findTable(tableNumber);
        if (table == null)
            return false;
        MenuItem menuItem = findMenuItem(itemId);
        Double updatedQuantity = menuItem.getQuantityInStock();
        int updatedTotalSold = menuItem.getTotalSold();

        if (table != null && menuItem != null && menuItem.getQuantityInStock() >= quantity) {
            OrderItem orderItem = new OrderItem(menuItem.getItemId(), quantity, tableNumber);
            table.addOrderItem(tableNumber, orderItem);
            updatedQuantity -= quantity;
            menuItem.setQuantityInStock(updatedQuantity);
            updatedTotalSold += quantity;
            menuItem.setTotalSold(updatedTotalSold);
            // update quantity for menu
            menuItem.updateMenuItem(menuItem);
            // insert order to orders table
            orderItem.setQuantity(quantity);
            orderItem.insertOrder();
            System.out.println("Order placed successfully.");
            return true;
        } else if (menuItem != null && menuItem.getQuantityInStock() < quantity) {
            System.out.println("Insufficient stock for item: " + itemId);
            return false;
        } else {
            System.out.println("Invalid table number or menu item.");
            return false;
        }
    }

    public void cancelOrder(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table == null) {
            System.out.println("Invalid table number or Table not found.");
        } else {
            String deleteQuery = "DELETE FROM Orders WHERE table_number = ? AND paid = 0;";
            try (Connection connection = DriverManager.getConnection(connectionString);
                    PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                // Set values for the prepared statement
                stmt.setInt(1, tableNumber);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("All Orders for tableNumber" + tableNumber + "Where closed successfully");
                } else
                    System.out.println("No Orders found for tablenumber:" + tableNumber);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeOrder(int tableNumber, int itemId, int wantedQuantity) {
        MenuItem menuItem = findMenuItem(itemId);
        String updateQuery = "UPDATE Orders SET quantity = ? WHERE table_number = ? AND  menu_item_id = ? AND paid = 0";
        List<ProductOrdered> current_orders = getCurrentMenuItemsForTable(tableNumber);
        double prevQuantity = 0;
        for (ProductOrdered productOrdered : current_orders) {
            if (menuItem.getItemName().equals(productOrdered.name)) {
                prevQuantity = productOrdered.quantity;
                break;
            }
        }
        if (wantedQuantity < 0)
            return;

        if (prevQuantity > 0) {
            if (prevQuantity < wantedQuantity) {
                double toAdd = wantedQuantity - prevQuantity;
                if (toAdd > menuItem.getQuantityInStock()) {
                    System.out.println("No enough to update!");
                    return;
                } else {
                    updatedQuantity(menuItem.getItemId(), menuItem.getQuantityInStock() - toAdd);
                    updateTotalSold(menuItem.getItemId(), menuItem.getTotalSold() + (int) toAdd);
                }

            } else {
                updatedQuantity(itemId, menuItem.getQuantityInStock() + (prevQuantity - wantedQuantity));
                updateTotalSold(menuItem.getItemId(), menuItem.getTotalSold() - (int) (prevQuantity - wantedQuantity));
            }
        } else {
            if (wantedQuantity > menuItem.getQuantityInStock()) {
                System.out.println("No enough available for: " + itemId);
                return;
            }
            this.placeOrder(tableNumber, itemId, wantedQuantity);
            return;
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setInt(1, wantedQuantity);
            stmt.setInt(2, tableNumber);
            stmt.setInt(3, menuItem.getItemId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {

                System.out.println("Order changed successfully.");

            } else {
                System.out.println("Item not found in order: Table Numer:" + tableNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkLowStock() {
        String query = "SELECT * FROM MenuItems;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int quantityInStock = resultSet.getInt("quantity_in_stock");
                int expectedAmount = resultSet.getInt("expectedAmount");
                if (quantityInStock < LOW_STOCK_THRESHOLD * expectedAmount) {
                    System.out.println("Low stock alert: " + itemName);
                } else {
                    System.out.println("stock is enough for item: " + itemName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public List<OrderHistory> getSumOfOrders(){
        String query = "SELECT menu_item_id, sum(quantity) as cnt FROM easycoffee.Orders WHERE paid = 1 group by menu_item_id;";
        List<OrderHistory> listOfItems = new ArrayList<OrderHistory>();
        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("menu_item_id");
                MenuItem menuItem = this.findMenuItem(itemId);
                int counter = resultSet.getInt("cnt");
                listOfItems.add(new OrderHistory(itemId, menuItem.getItemName(), counter));
            }
            return listOfItems;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void generateMonthlySalesReport() {
        System.out.println("Monthly Sales Report:");
        String query = "SELECT * FROM MenuItems;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int totalSold = resultSet.getInt("total_sold");
                System.out.println(itemName + " - Total Sold: " + totalSold);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateMonthlySalesGraph() {
        System.out.println(); // adding space before generating the graph
        System.out.println("Monthly Sales Graph:");
        /*
         * for (MenuItem menuItem : menu) {
         * 
         * }
         */

        String query = "SELECT * FROM MenuItems;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int totalSold = resultSet.getInt("total_sold");
                System.out.print(itemName + ": ");
                for (int i = 0; i < totalSold; i++) {
                    System.out.print("*");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String closeTable(int tableNumber) {
        String updateQuery = "UPDATE Tables SET is_occupied = ? WHERE table_number = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setInt(1, 0);
            stmt.setInt(2, tableNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return " " + tableNumber + " is Available For New Orders";
            } else {
                return "Could'nt Free table" + tableNumber + "After Payment";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void recordPayment(int tableNumber) {
        String updateQuery = "UPDATE Orders SET delivery_date = sysdate(), paid = 1 WHERE table_number = ? AND paid = 0";

        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            LocalDateTime deliveryDateTime = LocalDateTime.now();

            stmt.setInt(1, tableNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order is paied for table" + tableNumber);
                System.out.println("Payment recorded at: " + deliveryDateTime);
                closeTable(tableNumber);
            } else {
                System.out.println("Could'nt find orders for table" + tableNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Table findTable(int tableNumber) {
        String query = "SELECT * FROM Tables where table_number = ? ;";
        Table table = null;
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set values for the prepared statement
            stmt.setInt(1, tableNumber);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                table = new Table(tableNumber, resultSet.getInt(3));
                System.out.println("Found Table: " + tableNumber);
            } else {
                System.err.println("Table: " + tableNumber + "Not Found in DB.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }

    private MenuItem findMenuItem(int itemIdToFind) {
        MenuItem menuItem = null;
        String query = "SELECT * FROM MenuItems WHERE item_id = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set values for the prepared statement
            stmt.setInt(1, itemIdToFind);
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

                menuItem = new MenuItem(itemId, name, price, quantityInStock, totalSold, discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItem;
    }

    public Manager getManager(String managerId) {
        String query = "SELECT * FROM Manager WHERE manager_id = ?";
        Manager manager = null;
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set values for the prepared statement
            stmt.setString(1, managerId);
            ResultSet resultSet = stmt.executeQuery();

            // Check if a result was returned
            if (resultSet.next()) {
                // Retrieve data from the result set and create a MenuItem object
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                manager = new Manager(managerId, firstName, lastName, phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return manager;

    }

    public List<ProductOrdered> getCurrentMenuItemsForTable(int tableNumer) {
        List<ProductOrdered> menuItemsList = new java.util.ArrayList<ProductOrdered>();
        String query = "SELECT * FROM Orders WHERE paid = 0 AND table_number = ?;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tableNumer);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("menu_item_id");
                int quantity = resultSet.getInt("quantity");
                MenuItem menuItem = findMenuItem(itemId);
                menuItemsList.add(new ProductOrdered(menuItem.getItemId(), menuItem.getItemName(), menuItem.getPrice(),
                        quantity));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItemsList;
    }

    public boolean applyDiscount(String managerId, int itemId, double discountPercentage) {
        Manager currentManager = getManager(managerId);
        MenuItem menuItem = findMenuItem(itemId);

        if (menuItem != null && currentManager != null) {
            return menuItem.setDiscount(itemId, discountPercentage);
        } else {
            return false;
        }
    }

    public boolean updatedQuantity(int itemId, double quantity) {
        String updateQuery = "UPDATE MenuItems SET quantity_in_stock = ? WHERE item_id = ?";
        MenuItem menuItem = findMenuItem(itemId);
        if (menuItem == null) {
            return false;
        }
        Double oldQty = menuItem.getQuantityInStock();
        System.out.println("Menu Item:" + menuItem.getItemId());
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setDouble(1, quantity);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item: " + menuItem.getItemId() + " changed successfully, old: " + oldQty + " new: "
                        + quantity);
                return true;
            } else {
                System.out.println("Item " + itemId + "not found");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTotalSold(int itemId, int quantity) {
        String updateQuery = "UPDATE MenuItems SET total_sold = ? WHERE item_id = ?";
        MenuItem menuItem = findMenuItem(itemId);
        if (menuItem == null) {
            return false;
        }
        int oldQty = menuItem.getTotalSold();
        System.out.println("Menu Item:" + menuItem.getItemId());
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item: " + menuItem.getItemId() + " changed successfully, old: " + oldQty + " new: "
                        + quantity);
                return true;
            } else {
                System.out.println("Item " + itemId + "not found");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNumberOfTables() {
        int numberOfTables = 0;

        String query = "SELECT COUNT(*) as count FROM easycoffee.Tables;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                numberOfTables = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfTables;
    }

    public List<ProductOrdered> getAllMenuItem() {
        List<ProductOrdered> items = new java.util.ArrayList<ProductOrdered>();

        String query = "SELECT item_id, item_name, price, quantity_in_stock, discount FROM MenuItems WHERE quantity_in_stock >= 0;";
        try (Connection connection = DriverManager.getConnection(connectionString);
                PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                double itemPrice = resultSet.getDouble("price");
                String itemName = resultSet.getString("item_name");
                double quantity = resultSet.getDouble("quantity_in_stock");
                double itemDiscount = resultSet.getDouble("discount");
                ProductOrdered menuItem = new ProductOrdered(itemId, itemName, itemPrice, quantity, itemDiscount);

                System.out.println(menuItem.toString());
                items.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
class OrderHistory{
    int itemId;
    int counter;
    String itemName;
    public OrderHistory(int itemId, String itemName, int counter){
        this.counter = counter;
        this.itemId = itemId;
        this.itemName = itemName;
    }
}
// Define product as name and price
class ProductOrdered {
    int id;
    String name;
    double price;
    double quantity;
    double discount;

    public ProductOrdered(int id, String name, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = 0;
    }

    public ProductOrdered(int id, String name, double price, double quantity, double discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }
}
