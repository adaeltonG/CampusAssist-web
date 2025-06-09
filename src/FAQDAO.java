package src;

import java.sql.*;
import java.util.*;

public class FAQDAO {
    public static List<Map<String, String>> getAllFaqs() {
        List<Map<String, String>> faqs = new ArrayList<>();
        String sql = "SELECT * FROM faq ORDER BY popularity DESC, id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> faq = new HashMap<>();
                faq.put("id", String.valueOf(rs.getInt("id")));
                faq.put("question", rs.getString("question"));
                faq.put("answer", rs.getString("answer"));
                faq.put("category", rs.getString("category"));
                faq.put("popularity", String.valueOf(rs.getInt("popularity")));
                faqs.add(faq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faqs;
    }

    public static boolean addFaq(String question, String answer, String category) {
        String sql = "INSERT INTO faq (question, answer, category, popularity) VALUES (?, ?, ?, 0)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question);
            stmt.setString(2, answer);
            stmt.setString(3, category);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateFaq(int id, String question, String answer, String category) {
        String sql = "UPDATE faq SET question = ?, answer = ?, category = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question);
            stmt.setString(2, answer);
            stmt.setString(3, category);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFaq(int id) {
        String sql = "DELETE FROM faq WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Map<String, String>> searchFaqs(String keyword, String category, String sortBy) {
        List<Map<String, String>> faqs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM faq WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (question LIKE ? OR answer LIKE ?)");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND category = ?");
        }
        if ("popularity".equals(sortBy)) {
            sql.append(" ORDER BY popularity DESC, id DESC");
        } else if ("newest".equals(sortBy)) {
            sql.append(" ORDER BY id DESC");
        } else {
            sql.append(" ORDER BY id DESC");
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                stmt.setString(idx++, "%" + keyword + "%");
                stmt.setString(idx++, "%" + keyword + "%");
            }
            if (category != null && !category.trim().isEmpty()) {
                stmt.setString(idx++, category);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> faq = new HashMap<>();
                    faq.put("id", String.valueOf(rs.getInt("id")));
                    faq.put("question", rs.getString("question"));
                    faq.put("answer", rs.getString("answer"));
                    faq.put("category", rs.getString("category"));
                    faq.put("popularity", String.valueOf(rs.getInt("popularity")));
                    faqs.add(faq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faqs;
    }
} 