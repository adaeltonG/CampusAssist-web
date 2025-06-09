package src;

import src.AppointmentDAO;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// @WebServlet("/AdminActionServlet")
public class AdminActionServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        DBUtil.setDbUrl(dbPath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        String notification = request.getParameter("notification");
        String newDate = request.getParameter("new_date");
        String newTime = request.getParameter("new_time");
        boolean success = false;

        System.out.println("[DEBUG] AdminActionServlet: id=" + id + ", action=" + action);

        // Defensive check for id
        if (id == null || id.equals("null") || id.isEmpty()) {
            response.sendRedirect("admin_dashboard.jsp?error=missing_id");
            return;
        }

        try {
            int apptId = Integer.parseInt(id);
            if ("approve".equalsIgnoreCase(action)) {
                success = AppointmentDAO.updateAppointmentStatus(apptId, "approved", notification);
            } else if ("cancel".equalsIgnoreCase(action)) {
                success = AppointmentDAO.updateAppointmentStatus(apptId, "cancelled", notification);
            } else if ("reschedule".equalsIgnoreCase(action) && newDate != null && newTime != null) {
                String newDatetime = newDate + " " + newTime;
                success = AppointmentDAO.rescheduleAppointment(apptId, newDatetime, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            response.sendRedirect("admin_dashboard.jsp?success=1");
        } else {
            response.sendRedirect("admin_dashboard.jsp?error=1");
        }
    }
}
