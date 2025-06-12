package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import src.AppointmentDAO;

public class BookAppointmentServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        DBUtil.setDbUrl(dbPath);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
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
