package src;

import java.sql.*;

import src.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

public class StudentDAO {
    public static boolean validate(String username, String password) {
        String sql = "SELECT password FROM students WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
