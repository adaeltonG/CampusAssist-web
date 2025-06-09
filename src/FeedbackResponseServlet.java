package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FeedbackResponseServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        System.out.println("[DEBUG] DB PATH: " + dbPath);
        DBUtil.setDbUrl(dbPath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String adminResponse = request.getParameter("admin_response");
        boolean success = false;
        try {
            int feedbackId = Integer.parseInt(id);
            success = FeedbackDAO.updateAdminResponse(feedbackId, adminResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[DEBUG] FeedbackResponseServlet: id=" + id + ", response=" + adminResponse + ", success=" + success);
        response.sendRedirect("admin_dashboard.jsp");
    }
} 