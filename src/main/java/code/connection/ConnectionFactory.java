package code.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/to_do_list";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }
}
