package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FeedbackCategoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String category = request.getParameter("category");
        // Here you would update the feedback category in the database
        System.out.println("[DEBUG] FeedbackCategoryServlet: id=" + id + ", category=" + category);
        response.sendRedirect("admin_dashboard.jsp");
    }
} 