package src;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordMigrationUtility {
    public static void main(String[] args) {
        // Set DB path (adjust if needed)
        String dbPath = "./WEB-INF/campusassist.db";
        DBUtil.setDbUrl(dbPath);
        migrateTable("students");
        migrateTable("admins");
        System.out.println("Password migration completed.");
    }

    private static void migrateTable(String tableName) {
        String selectSql = "SELECT id, password FROM " + tableName;
        String updateSql = "UPDATE " + tableName + " SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             ResultSet rs = selectStmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String plainPassword = rs.getString("password");
                // Skip if already hashed (bcrypt hashes start with $2)
                if (plainPassword != null && !plainPassword.startsWith("$2")) {
                    String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, hashed);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        System.out.println("Updated " + tableName + " id=" + id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 