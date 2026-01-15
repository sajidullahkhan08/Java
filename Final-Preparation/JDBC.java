// Sample Code of JDBC Connection in Java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*Steps in Order:
1. Load Driver
2. Establish connection
3. Create Statement and execute query
4. Result set
 */

public class JDBC {
    public static void main(String[] args) {
        try {
            // Step 1: Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");

            // Step 2: Establish connection
            String url = "jdbc:mysql://localhost:3306/testdb";
            String user = "root";
            String password = "password";
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");

            // Step 3: Create Statement and execute query
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(query);

            // Step 4: Process Result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // Close resources
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}