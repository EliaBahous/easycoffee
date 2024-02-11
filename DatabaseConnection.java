public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private String connectionString;

    private DatabaseConnection() {
        // Initialize your connection string here
        connectionString =
        "jdbc:sqlserver://metadatasqldb.database.windows.net:1433;"
                + "database=easycafe;"
                + "user=metadatasqldb;"
                + "password=Uh995512.;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";
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
