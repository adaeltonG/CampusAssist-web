package src;

import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    public static boolean bookAppointment(String username, String category, String datetime) {
        String sql = "INSERT INTO appointments (username, category, datetime, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, category);
            stmt.setString(3, datetime);
            stmt.setString(4, "pending");
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateAppointmentStatus(int apptId, String status, String notification) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, apptId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean rescheduleAppointment(int apptId, String newDatetime, String notification) {
        String sql = "UPDATE appointments SET datetime = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDatetime);
            stmt.setInt(2, apptId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Map<String, String>> getAllAppointments() {
        List<Map<String, String>> appts = new ArrayList<>();
        String sql = "SELECT id, username, category, datetime, status FROM appointments ORDER BY datetime DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> appt = new HashMap<>();
                appt.put("id", String.valueOf(rs.getInt("id")));
                appt.put("username", rs.getString("username"));
                appt.put("category", rs.getString("category"));
                appt.put("datetime", rs.getString("datetime"));
                appt.put("status", rs.getString("status"));
                appt.put("notification", "");
                appts.add(appt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appts;
    }
    public static List<Map<String, String>> getAppointmentsForStudent(String username) {
        List<Map<String, String>> appts = new ArrayList<>();
        String sql = "SELECT a.id, a.username, a.category, a.datetime, a.status, a.notification, f.feedback_text FROM appointments a LEFT JOIN feedback f ON a.id = f.appointment_id WHERE a.username = ? ORDER BY a.datetime DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> appt = new HashMap<>();
                    appt.put("id", String.valueOf(rs.getInt("id")));
                    appt.put("username", rs.getString("username"));
                    appt.put("category", rs.getString("category"));
                    appt.put("datetime", rs.getString("datetime"));
                    appt.put("status", rs.getString("status"));
                    appt.put("notification", rs.getString("notification"));
                    String feedbackText = rs.getString("feedback_text");
                    appt.put("feedback", feedbackText == null ? "" : feedbackText);
                    appts.add(appt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appts;
    }
} 