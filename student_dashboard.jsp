<%@ page import="java.util.*" %>
<%@ page import="src.AppointmentDAO" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="src.FeedbackDAO" %>
<%
    String username = (String) session.getAttribute("username");
    List<Map<String, String>> appts = src.AppointmentDAO.getAppointmentsForStudent(username);
    List<Map<String, String>> feedbacks = src.FeedbackDAO.getFeedbackForStudent(username);
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter displayDtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="header-section">
            <h1>Student Dashboard</h1>
        </div>
        
        <div class="student-feedback-section">
            <h2>Your Feedback & Admin Responses</h2>
            <table class="feedback-table" width="100%">
                <tr>
                    <th>Category</th>
                    <th>Your Feedback</th>
                    <th>Admin Response</th>
                </tr>
                <% for (Map<String, String> fb : feedbacks) { %>
                    <tr>
                        <td><%= fb.get("category") %></td>
                        <td><%= fb.get("feedback_text") %></td>
                        <td>
                            <% if (fb.get("admin_response") != null && !fb.get("admin_response").trim().isEmpty()) { %>
                                <span class="admin-response-cell"><%= fb.get("admin_response") %></span>
                            <% } else { %>
                                <span style="color: #aaa;">(No response yet)</span>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </table>
        </div>
        
        <div class="appointments-section">
            <h2>Your Appointments</h2>
            <div class="table-wrapper">
                <table class="student-table" border="1" width="100%">
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Date/Time</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> appt : appts) {
                            String status = appt.get("status");
                            if ("completed".equalsIgnoreCase(status)) continue; // Skip completed appointments
                        %>
                            <tr>
                                <td><%= appt.get("category") %></td>
                                <td>
                                <%
                                    String datetime = appt.get("datetime");
                                    LocalDateTime apptTime = null;
                                    try {
                                        apptTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                                    } catch (Exception e) {
                                        try { apptTime = LocalDateTime.parse(datetime.replace(" ", "T")); } catch (Exception ex) { }
                                    }
                                    if (apptTime != null) {
                                        out.print(apptTime.format(displayDtf));
                                    } else {
                                        out.print(datetime);
                                    }
                                %>
                                </td>
                                <td class="status-cell">
                                    <%
                                        String statusLabel = "-";
                                        String statusColor = "";
                                        if (status != null) {
                                            if ("approved".equalsIgnoreCase(status)) {
                                                statusLabel = "Approved";
                                                statusColor = "background:#c8e6c9;color:#256029;padding:3px 8px;border-radius:6px;font-weight:bold;";
                                            } else if ("cancelled".equalsIgnoreCase(status)) {
                                                statusLabel = "Cancelled";
                                                statusColor = "background:#ffcdd2;color:#b71c1c;padding:3px 8px;border-radius:6px;font-weight:bold;";
                                            } else if ("pending".equalsIgnoreCase(status)) {
                                                statusLabel = "Pending";
                                                statusColor = "background:#ffe0b2;color:#e65100;padding:3px 8px;border-radius:6px;font-weight:bold;";
                                            } else {
                                                statusLabel = status;
                                            }
                                        }
                                    %>
                                    <span style="<%= statusColor %>"><%= statusLabel %></span>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="booking-section">
            <h2>Book a New Appointment</h2>
            <div class="form-content">
                <form action="BookAppointmentServlet" method="post" id="bookingForm" class="booking-form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="category">Category:</label>
                            <select name="category" id="category" required>
                                <option value="Academic">Academic</option>
                                <option value="Financial">Financial</option>
                                <option value="Mental Health">Mental Health</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group half-width">
                            <label for="date">Date:</label>
                            <input type="date" id="date" name="datetime_date" required>
                        </div>
                        <div class="form-group half-width">
                            <label for="time">Time:</label>
                            <select id="time" name="datetime_time" required>
                                <option value="09:00">09:00</option>
                                <option value="10:00">10:00</option>
                                <option value="11:00">11:00</option>
                                <option value="12:00">12:00</option>
                                <option value="13:00">13:00</option>
                                <option value="14:00">14:00</option>
                                <option value="15:00">15:00</option>
                                <option value="16:00">16:00</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Book Appointment</button>
                    </div>
                </form>
            </div>
        </div>
        
        <div class="feedback-section">
            <h2>Submit Feedback for Completed Appointment</h2>
            <div class="form-content">
                <form action="SubmitFeedbackServlet" method="post" class="feedback-form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="appointment_id">Appointment:</label>
                            <select name="appointment_id" id="appointment_id" required>
                                <%
                                    for (Map<String, String> appt : appts) {
                                        String status = appt.get("status");
                                        String datetime = appt.get("datetime");
                                        String feedback = appt.get("feedback");
                                        LocalDateTime apptTime = null;
                                        try {
                                            apptTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                                        } catch (Exception e) {
                                            try { apptTime = LocalDateTime.parse(datetime.replace(" ", "T")); } catch (Exception ex) { }
                                        }
                                        if ("completed".equalsIgnoreCase(status) && apptTime != null && apptTime.isBefore(now) && (feedback == null || feedback.isEmpty())) {
                                %>
                                    <option value="<%= appt.get("id") %>"><%= appt.get("category") %> on <%
                                        if (apptTime != null) {
                                            out.print(apptTime.format(displayDtf));
                                        } else {
                                            out.print(datetime);
                                        }
                                    %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="category">Category:</label>
                            <div class="category-display">
                                <span id="feedback_category_display"></span>
                                <input type="hidden" name="category" id="feedback_category_hidden" />
                            </div>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="feedback_text">Feedback:</label>
                            <textarea name="feedback_text" id="feedback_text" rows="4" required placeholder="Please share your feedback about the appointment..."></textarea>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-primary">Submit Feedback</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        // Generate the apptCategories JS object using JSP (fully robust)
        var apptCategories = {
<% 
    boolean first = true;
    for (Map<String, String> appt : appts) {
        String status = appt.get("status");
        String datetime = appt.get("datetime");
        LocalDateTime apptTime = null;
        try {
            apptTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            try { apptTime = LocalDateTime.parse(datetime.replace(" ", "T")); } catch (Exception ex) { }
        }
        if ("completed".equalsIgnoreCase(status) && apptTime != null && apptTime.isBefore(now)) {
            if (!first) { out.print(",\n"); }
            String cat = appt.get("category").replace("\\", "\\\\").replace("'", "\\'");
            out.print("'" + appt.get("id") + "':'" + cat + "'");
            first = false;
        }
    }
%>
        };
        
        function updateCategoryDisplay() {
            var sel = document.getElementById('appointment_id');
            var cat = apptCategories[sel && sel.value ? sel.value : ''] || '';
            document.getElementById('feedback_category_display').textContent = cat;
            document.getElementById('feedback_category_hidden').value = cat;
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            var sel = document.getElementById('appointment_id');
            if (sel) {
                sel.addEventListener('change', updateCategoryDisplay);
                updateCategoryDisplay(); // set initial value
            }
            
            // Restrict date input to Monday-Friday, June to December
            const dateInput = document.getElementById('date');
            const today = new Date();
            let year = today.getFullYear();
            // Set min date to next June if before June, otherwise this June
            let minMonth = today.getMonth() < 5 ? 5 : today.getMonth();
            let minDate = new Date(year, minMonth, 1);
            let maxDate = new Date(year, 11, 31); // December 31
            
            dateInput.min = minDate.toISOString().split('T')[0];
            dateInput.max = maxDate.toISOString().split('T')[0];
            
            dateInput.addEventListener('input', function() {
                const d = new Date(this.value);
                // 0 = Sunday, 6 = Saturday
                if (d.getDay() === 0 || d.getDay() === 6) {
                    alert('Please select a weekday (Monday to Friday).');
                    this.value = '';
                }
                // Only allow June to December
                if (d.getMonth() < 5 || d.getMonth() > 11) {
                    alert('Please select a month from June to December.');
                    this.value = '';
                }
            });
        });
    </script>
</body>
</html>