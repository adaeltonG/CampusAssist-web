package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import src.FeedbackDAO;

// @WebServlet("/SubmitFeedbackServlet")
public class SubmitFeedbackServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        DBUtil.setDbUrl(dbPath);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        if (username == null) {
            response.sendRedirect("index.html?error=not_logged_in");
            return;
        }
        String appointmentIdStr = request.getParameter("appointment_id");
        String category = request.getParameter("category");
        String feedbackText = request.getParameter("feedback_text");
        int appointmentId = Integer.parseInt(appointmentIdStr);
        boolean success = FeedbackDAO.addFeedback(appointmentId, username, category, feedbackText);
        if (success) {
            response.sendRedirect("student_dashboard.jsp?feedback=success");
        } else {
            response.sendRedirect("student_dashboard.jsp?feedback=error");
        }
    }
}
