public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private String connectionString;

    private DatabaseConnection() {
        // Initialize your connection string here
        connectionString = "jdbc:mysql://localhost/easycoffee?user=root&password=yourpasswd";
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
