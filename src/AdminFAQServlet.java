package src;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AdminFAQServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String question = request.getParameter("question");
            String answer = request.getParameter("answer");
            String category = request.getParameter("category");
            FAQDAO.addFaq(question, answer, category);
        } else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                FAQDAO.deleteFaq(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("update".equals(action)) {
            String idStr = request.getParameter("id");
            String question = request.getParameter("question");
            String answer = request.getParameter("answer");
            String category = request.getParameter("category");
            try {
                int id = Integer.parseInt(idStr);
                FAQDAO.updateFaq(id, question, answer, category);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("admin_dashboard.jsp");
    }
} 