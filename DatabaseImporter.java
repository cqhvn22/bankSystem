import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.nio.charset.StandardCharsets;

public class DatabaseImporter {
    public static void main(String[] args) {
        String url = "jdbc:mysql://shuttle.proxy.rlwy.net:33315/railway?allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "veBCoLldsBaOezZfeWVorooJWlTMSZxP";
        String filePath = "db_dump.sql";

        try {
            System.out.println("Reading SQL file...");
            String sql = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            
            System.out.println("Connecting to database...");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {
                
                System.out.println("Executing SQL...");
                // Note: Multi-queries allowed via URL parameter
                stmt.execute(sql);
                System.out.println("Import successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
