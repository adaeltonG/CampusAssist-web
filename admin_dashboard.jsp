<%@ page import="java.util.*" %>
<%@ page import="src.AppointmentDAO" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="src.FeedbackDAO" %>
<%@ page import="src.FAQDAO" %>
<%
    List<Map<String, String>> appts = src.AppointmentDAO.getAllAppointments();
    DateTimeFormatter displayDtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    List<Map<String, String>> feedbacks = src.FeedbackDAO.getAllFeedback();
    Map<String, Integer> trends = src.FAQDAO.getFaqTrends();
    String[] categories = {"Academic", "Financial", "Mental Health"};
    List<Map<String, String>> faqs = src.FAQDAO.getAllFaqs();
    String apptStudent = request.getParameter("apptStudent");
    String apptCategory = request.getParameter("apptCategory");
    String apptStatus = request.getParameter("apptStatus");
    String apptDateFrom = request.getParameter("apptDateFrom");
    String apptDateTo = request.getParameter("apptDateTo");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="header-section">
            <h1>Admin Dashboard</h1>
        </div>
        
        <div class="appointments-section">
            <h2>All Appointments</h2>
            <form method="get" class="appt-search-form" style="margin-bottom: 1em;">
                <input type="text" name="apptStudent" placeholder="Student Username" value="<%= apptStudent != null ? apptStudent : "" %>" />
                <select name="apptCategory">
                    <option value="">All Categories</option>
                    <option value="Academic" <%= "Academic".equals(apptCategory) ? "selected" : "" %>>Academic</option>
                    <option value="Financial" <%= "Financial".equals(apptCategory) ? "selected" : "" %>>Financial</option>
                    <option value="Mental Health" <%= "Mental Health".equals(apptCategory) ? "selected" : "" %>>Mental Health</option>
                </select>
                <select name="apptStatus">
                    <option value="">All Statuses</option>
                    <option value="pending" <%= "pending".equals(apptStatus) ? "selected" : "" %>>Pending</option>
                    <option value="approved" <%= "approved".equals(apptStatus) ? "selected" : "" %>>Approved</option>
                    <option value="completed" <%= "completed".equals(apptStatus) ? "selected" : "" %>>Completed</option>
                    <option value="cancelled" <%= "cancelled".equals(apptStatus) ? "selected" : "" %>>Cancelled</option>
                </select>
                <button type="submit" class="filter-btn">Filter</button>
            </form>
            <div class="table-wrapper">
                <table class="admin-table" width="100%">
                    <thead>
                        <tr>
                            <th>Student Username</th>
                            <th>Category</th>
                            <th>Date/Time</th>
                            <th>Status</th>
                            <th>Notification</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Map<String, String> appt : appts) {
                            String student = appt.get("username");
                            String category = appt.get("category");
                            String status = appt.get("status");
                            String datetime = appt.get("datetime");
                            boolean show = true;
                            if (apptStudent != null && !apptStudent.isEmpty() && (student == null || !student.toLowerCase().contains(apptStudent.toLowerCase()))) show = false;
                            if (apptCategory != null && !apptCategory.isEmpty() && (category == null || !category.toLowerCase().contains(apptCategory.toLowerCase()))) show = false;
                            if (apptStatus != null && !apptStatus.isEmpty() && (status == null || !status.equalsIgnoreCase(apptStatus))) show = false;
                            if (apptDateFrom != null && !apptDateFrom.isEmpty() && datetime != null && datetime.compareTo(apptDateFrom) < 0) show = false;
                            if (apptDateTo != null && !apptDateTo.isEmpty() && datetime != null && datetime.compareTo(apptDateTo) > 0) show = false;
                            if (!show) continue;
                        %>
                            <tr>
                                <td><%= student %></td>
                                <td><%= category %></td>
                                <td>
                                    <%
                                        java.time.LocalDateTime apptTime = null;
                                        try {
                                            apptTime = java.time.LocalDateTime.parse(datetime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                                        } catch (Exception e) {
                                            try { apptTime = java.time.LocalDateTime.parse(datetime.replace(" ", "T")); } catch (Exception ex) { }
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
                                            } else if ("completed".equalsIgnoreCase(status)) {
                                                statusLabel = "Completed";
                                                statusColor = "background:#b3e5fc;color:#01579b;padding:3px 8px;border-radius:6px;font-weight:bold;";
                                            } else {
                                                statusLabel = status;
                                            }
                                        }
                                    %>
                                    <span style="<%= statusColor %>"><%= statusLabel %></span>
                                </td>
                                <td><%= (appt.get("notification") == null || "null".equals(appt.get("notification"))) ? "-" : appt.get("notification") %></td>
                                <td class="actions-cell">
                                    <div class="admin-actions">
                                        <div class="action-row">
                                            <form action="AdminActionServlet" method="post" class="action-form">
                                                <input type="hidden" name="id" value="<%= appt.get("id") %>" />
                                                <input type="hidden" name="action" value="approve" />
                                                <input type="hidden" name="notification" value="Your appointment has been approved." />
                                                <button type="submit" class="btn-approve">Approve</button>
                                            </form>
                                            <form action="AdminActionServlet" method="post" class="action-form">
                                                <input type="hidden" name="id" value="<%= appt.get("id") %>" />
                                                <input type="hidden" name="action" value="cancel" />
                                                <input type="hidden" name="notification" value="Your appointment has been cancelled." />
                                                <button type="submit" class="btn-cancel">Cancel</button>
                                            </form>
                                        </div>
                                        <div class="action-row reschedule-row">
                                            <form action="AdminActionServlet" method="post" class="reschedule-form">
                                                <input type="hidden" name="id" value="<%= appt.get("id") %>" />
                                                <input type="hidden" name="action" value="reschedule" />
                                                <div class="reschedule-inputs">
                                                    <input type="date" name="new_date" id="reschedule_date_<%= appt.get("id") %>" required class="date-input" />
                                                    <select name="new_time" id="reschedule_time_<%= appt.get("id") %>" required class="time-select">
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
                                                <input type="hidden" name="notification" value="Your appointment has been rescheduled." />
                                                <button type="submit" class="btn-reschedule">Reschedule</button>
                                            </form>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <div class="container">
        <div class="feedback-section">
            <h2>Feedback & Reports</h2>
            
            <div class="trends-section">
                <h3>Feedback Trends</h3>
                <ul class="trends-list">
                <% for (String cat : categories) { %>
                    <li class="trend-item"><strong><%= cat %>:</strong> <%= trends.getOrDefault(cat, 0) %> submissions</li>
                <% } %>
                </ul>
            </div>
            
            <div class="feedback-table-section">
                <h3>All Feedback</h3>
                <div class="table-wrapper">
                    <table class="feedback-table" width="100%">
                        <thead>
                            <tr>
                                <th>Student</th>
                                <th>Category</th>
                                <th>Feedback</th>
                                <th>Admin Response</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map<String, String> fb : feedbacks) { %>
                                <tr>
                                    <td><%= fb.get("student_username") %></td>
                                    <td><%= fb.get("category") %></td>
                                    <td class="feedback-text"><%= fb.get("feedback_text") %></td>
                                    <td class="response-cell">
                                        <% if (fb.get("admin_response") != null && !fb.get("admin_response").trim().isEmpty()) { %>
                                            <div class="admin-response-cell"><%= fb.get("admin_response") %></div>
                                        <% } else { %>
                                            <button type="button" class="open-modal-btn" 
                                                data-fid="<%= fb.get("id") %>"
                                                data-student="<%= fb.get("student_username") %>"
                                                data-category="<%= fb.get("category") %>"
                                                data-feedback="<%= fb.get("feedback_text").replace("\"", "&quot;") %>">
                                                Respond
                                            </button>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <!-- Modal Dialog for Responding -->
        <div id="respondModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Respond to Feedback</h3>
                    <span id="closeModal" class="close-btn">&times;</span>
                </div>
                <div class="modal-body">
                    <div class="modal-info">
                        <div class="info-row"><strong>Student:</strong> <span id="modalStudent"></span></div>
                        <div class="info-row"><strong>Category:</strong> <span id="modalCategory"></span></div>
                        <div class="info-row"><strong>Feedback:</strong> <span id="modalFeedback"></span></div>
                    </div>
                    <form id="modalRespondForm" action="/Campusbooking/FeedbackResponseServlet" method="post" class="modal-form">
                        <input type="hidden" name="id" id="modalFeedbackId" />
                        <div class="form-group">
                            <label for="modalAdminResponse">Response:</label>
                            <input type="text" name="admin_response" id="modalAdminResponse" placeholder="Type response..." required />
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn-send">Send</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <div class="container">
        <div class="faq-section">
            <h2 id="faq-section">Manage FAQs</h2>
            <form action="AdminFAQServlet#faq-section" method="post" class="faq-form">
                <input type="hidden" name="action" value="add" />
                <input type="text" name="question" placeholder="Question" required />
                <input type="text" name="answer" placeholder="Answer" required />
                <select name="category" required>
                    <option value="Academic">Academic</option>
                    <option value="Financial">Financial</option>
                    <option value="Mental Health">Mental Health</option>
                </select>
                <button type="submit">Add FAQ</button>
            </form>
            <div class="faq-list">
                <% for (Map<String, String> faq : faqs) { %>
                    <div class="faq-card">
                        <div class="faq-question"><b>Q:</b> <%= faq.get("question") %></div>
                        <div class="faq-answer"><b>A:</b> <%= faq.get("answer") %></div>
                        <div class="faq-meta-row">
                            <span class="faq-meta"><b>Category:</b> <%= faq.get("category") %></span>
                            <span class="faq-meta"><b>Popularity:</b> <%= faq.get("popularity") %></span>
                            <div class="faq-actions">
                                <form action="AdminFAQServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="id" value="<%= faq.get("id") %>" />
                                    <button type="submit" class="faq-action-btn">Delete</button>
                                </form>
                                <button type="button" class="faq-action-btn" onclick="showEditForm('<%= faq.get("id") %>')">Edit</button>
                            </div>
                        </div>
                        <form id="edit-faq-form-<%= faq.get("id") %>" action="AdminFAQServlet" method="post" style="display:none; margin-top:8px;">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="id" value="<%= faq.get("id") %>" />
                            <input type="text" name="question" value="<%= faq.get("question") %>" required />
                            <input type="text" name="answer" value="<%= faq.get("answer") %>" required />
                            <input type="text" name="category" value="<%= faq.get("category") %>" required />
                            <button type="submit">Save</button>
                            <button type="button" onclick="hideEditForm('<%= faq.get("id") %>')">Cancel</button>
                        </form>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
    
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        // Modal logic
        var modal = document.getElementById('respondModal');
        var closeModal = document.getElementById('closeModal');
        var modalStudent = document.getElementById('modalStudent');
        var modalCategory = document.getElementById('modalCategory');
        var modalFeedback = document.getElementById('modalFeedback');
        var modalFeedbackId = document.getElementById('modalFeedbackId');
        var modalAdminResponse = document.getElementById('modalAdminResponse');
        
        document.querySelectorAll('.open-modal-btn').forEach(function(btn) {
            btn.addEventListener('click', function() {
                modal.style.display = 'flex';
                modalStudent.textContent = btn.getAttribute('data-student');
                modalCategory.textContent = btn.getAttribute('data-category');
                modalFeedback.textContent = btn.getAttribute('data-feedback');
                modalFeedbackId.value = btn.getAttribute('data-fid');
                modalAdminResponse.value = '';
                modalAdminResponse.focus();
            });
        });
        
        closeModal.onclick = function() { modal.style.display = 'none'; };
        window.onclick = function(event) { if (event.target == modal) modal.style.display = 'none'; };
        
        // Debug: log when the modal form is submitted
        document.getElementById('modalRespondForm').addEventListener('submit', function(e) {
            console.log('[DEBUG] Modal form submitted');
        });
        
        // Restrict reschedule date input to Monday-Friday, June to December
        const dateInputs = document.querySelectorAll('input[type="date"][id^="reschedule_date_"]');
        const today = new Date();
        let year = today.getFullYear();
        let minMonth = today.getMonth() < 5 ? 5 : today.getMonth();
        let minDate = new Date(year, minMonth, 1);
        let maxDate = new Date(year, 11, 31); // December 31
        
        dateInputs.forEach(function(dateInput) {
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
    });
    
    function showEditForm(id) {
        document.getElementById('edit-faq-form-' + id).style.display = 'block';
    }
    function hideEditForm(id) {
        document.getElementById('edit-faq-form-' + id).style.display = 'none';
    }
    </script>
</body>
</html>