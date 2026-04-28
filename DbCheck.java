import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbCheck {
    public static void main(String[] args) {
        String url = "jdbc:mysql://shuttle.proxy.rlwy.net:33315/railway?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "veBCoLldsBaOezZfeWVorooJWlTMSZxP";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Checking users table...");
            ResultSet rs = stmt.executeQuery("SELECT username FROM users");
            int count = 0;
            while (rs.next()) {
                System.out.println("Found user: " + rs.getString("username"));
                count++;
            }
            System.out.println("Total users: " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
