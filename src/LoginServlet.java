package src;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        String dbPath = getServletContext().getRealPath("/WEB-INF/campusassist.db");
        DBUtil.setDbUrl(dbPath);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        if (AdminDAO.validate(username, password)) {
            session.setAttribute("role", "admin");
            session.setAttribute("username", username);
            response.sendRedirect("admin_dashboard.jsp");
            return;
        } else if (StudentDAO.validate(username, password)) {
            session.setAttribute("role", "student");
            session.setAttribute("username", username);
            response.sendRedirect("student_dashboard.jsp");
            return;
        }
        response.sendRedirect("index.html?error=1");
    }
}
