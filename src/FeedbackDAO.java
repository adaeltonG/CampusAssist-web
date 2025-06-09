package src;

import java.sql.*;

public class FeedbackDAO {
    public static boolean addFeedback(int appointmentId, String username, String category, String feedbackText) {
        String selectSql = "SELECT username, category FROM appointments WHERE id = ?";
        String insertSql = "INSERT INTO feedback (appointment_id, student_username, category, feedback_text, created_at) VALUES (?, ?, ?, ?, datetime('now'))";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, appointmentId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    String studentUsername = rs.getString("username");
                    String apptCategory = rs.getString("category");
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, appointmentId);
                        insertStmt.setString(2, studentUsername);
                        insertStmt.setString(3, apptCategory);
                        insertStmt.setString(4, feedbackText);
                        int rows = insertStmt.executeUpdate();
                        return rows > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static java.util.List<java.util.Map<String, String>> getAllFeedback() {
        java.util.List<java.util.Map<String, String>> feedbacks = new java.util.ArrayList<>();
        String sql = "SELECT id, student_username, category, feedback_text, admin_response, created_at FROM feedback ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String, String> fb = new java.util.HashMap<>();
                fb.put("id", String.valueOf(rs.getInt("id")));
                fb.put("student_username", rs.getString("student_username"));
                fb.put("category", rs.getString("category"));
                fb.put("feedback_text", rs.getString("feedback_text"));
                fb.put("admin_response", rs.getString("admin_response"));
                fb.put("created_at", rs.getString("created_at"));
                feedbacks.add(fb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
    public static java.util.Map<String, Integer> getFeedbackTrends() {
        return new java.util.HashMap<>();
    }
    public static boolean updateAdminResponse(int feedbackId, String adminResponse) {
        String sql = "UPDATE feedback SET admin_response = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adminResponse);
            stmt.setInt(2, feedbackId);
            int rows = stmt.executeUpdate();
            System.out.println("[DEBUG] Executed: " + sql + " | admin_response=" + adminResponse + " | id=" + feedbackId + " | rows updated=" + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("[DEBUG] SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public static java.util.List<java.util.Map<String, String>> getFeedbackForStudent(String username) {
        java.util.List<java.util.Map<String, String>> feedbacks = new java.util.ArrayList<>();
        String sql = "SELECT id, category, feedback_text, admin_response, created_at FROM feedback WHERE student_username = ? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, String> fb = new java.util.HashMap<>();
                    fb.put("id", String.valueOf(rs.getInt("id")));
                    fb.put("category", rs.getString("category"));
                    fb.put("feedback_text", rs.getString("feedback_text"));
                    fb.put("admin_response", rs.getString("admin_response"));
                    fb.put("created_at", rs.getString("created_at"));
                    feedbacks.add(fb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
}

 