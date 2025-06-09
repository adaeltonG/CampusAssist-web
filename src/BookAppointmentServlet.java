package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import src.AppointmentDAO;

// @WebServlet("/BookAppointmentServlet")
public class BookAppointmentServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        DBUtil.setDbUrl(dbPath);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create if not exists
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        System.out.println("[DEBUG] Session ID: " + (session != null ? session.getId() : "null"));
        System.out.println("[DEBUG] Username in session: " + username);
        if (username == null) {
            System.out.println("[DEBUG] Username is null, redirecting to login.");
            response.sendRedirect("index.html?error=not_logged_in");
            return;
        }
        String category = request.getParameter("category");
        String date = request.getParameter("datetime_date");
        String time = request.getParameter("datetime_time");
        String datetime = date + " " + time;
        if (AppointmentDAO.bookAppointment(username, category, datetime)) {
            response.sendRedirect("student_dashboard.jsp?success=1");
        } else {
            response.sendRedirect("student_dashboard.jsp?error=1");
        }
    }
}
