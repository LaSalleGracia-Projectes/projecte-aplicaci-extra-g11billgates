package org.example.teamup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectDB {
    private static Connection instance;
    private ConnectDB() {}
    public static Connection getInstance() {
        if (instance == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/testdb";
                String user = "root";
                String password = "";
                instance = DriverManager.getConnection(url, user,
                        password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
