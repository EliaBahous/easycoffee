import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {
    // Connect to your database.
    // Replace server name, username, and password with your credentials

    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String DB_URL = "jdbc:sqlserver://metadatasqldb.database.windows.net:1433";
    
    static final String USER = "metadatasqldb@metadatasqldb";
    static final String PASS = "Uh995512.";
    public static void main(String[] args) {
        String connectionUrl =
                "jdbc:sqlserver://metadatasqldb.database.windows.net:1433;"
                        + "database=easycafe;"
                        + "user=metadatasqldb;"
                        + "password=Uh995512.;"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        ResultSet resultSet = null;
            
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();) {
               // Create and execute a SELECT SQL statement.
               String selectSql = "SELECT * from dbo.MenuItems";
               resultSet = statement.executeQuery(selectSql);
                 // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}