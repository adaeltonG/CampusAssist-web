//THIS CODE WAS WRITTEN BY ADAELTON GOUART //NARCIS STOICA
package src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusAssistSystem {
    private Map<String, Student> students; // Store students by ID for easy lookup
    private Map<String, Admin> admins;
    private List<Appointment> appointments; // Central list of all appointments

    public CampusAssistSystem() {
        students = new HashMap<>();
        admins = new HashMap<>();
        appointments = new ArrayList<>();
        loadHardcodedData();
    }

    private void loadHardcodedData() {
        // Create some students
        Student student1 = new Student("S1001", "Adaelton Goulart");
        Student student2 = new Student("S1002", "Narcis Stoica");
        students.put(student1.getStudentId(), student1);
        students.put(student2.getStudentId(), student2);

        // Create some admin users
        Admin admin1 = new Admin("A1001", "Chathurika", "Chathurika@university.edu", "Support Staff");
        Admin admin2 = new Admin("A1002", "Goonawardane", "Goonawardane@university.edu", "System Admin");
        admins.put(admin1.getAdminId(), admin1);
        admins.put(admin2.getAdminId(), admin2);

        // Create some appointments (past and upcoming)
        LocalDateTime now = LocalDateTime.now();

        // Past appointment for Alice (completed)
        Appointment pastAppt1 = new Appointment(student1, Category.ACADEMIC_SUPPORT, now.minusDays(10));
        pastAppt1.setStatus(AppointmentStatus.COMPLETED);
        appointments.add(pastAppt1);
        student1.addAppointment(pastAppt1);

        // Upcoming appointment for Alice
        Appointment upcomingAppt1 = new Appointment(student1, Category.MENTAL_HEALTH, now.plusDays(2).withHour(10).withMinute(0));
        appointments.add(upcomingAppt1);
        student1.addAppointment(upcomingAppt1);

        // Upcoming appointment for Bob (within 24 hours)
        Appointment upcomingAppt2 = new Appointment(student2, Category.FINANCIAL_AID, now.plusHours(5));
        appointments.add(upcomingAppt2);
        student2.addAppointment(upcomingAppt2);

        System.out.println("Hardcoded data loaded: " + students.size() + " students, " + 
                         admins.size() + " admins, " + appointments.size() + " appointments.");
    }

    // Login methods
    public Object login(String userId) {
        if (userId.startsWith("S")) {
            return students.get(userId);
        } else if (userId.startsWith("A")) {
            return admins.get(userId);
        }
        return null;
    }

    public boolean isAdmin(Object user) {
        return user instanceof Admin;
    }

    // Student methods
    public List<Appointment> getAppointmentsForStudent(Student student) {
        if (student == null) {
            return new ArrayList<>();
        }
        return student.getAppointments();
    }

    // Admin methods
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean approveAppointment(String appointmentId, Admin admin) {
        Appointment appointment = findAppointment(appointmentId);
        if (appointment != null) {
            return admin.approveAppointment(appointment);
        }
        return false;
    }

    public boolean cancelAppointment(String appointmentId, Object user) {
        Appointment appointment = findAppointment(appointmentId);
        if (appointment == null) return false;

        if (user instanceof Admin) {
            return ((Admin) user).cancelAppointment(appointment);
        } else if (user instanceof Student) {
            Student student = (Student) user;
            if (appointment.getStudent().equals(student)) {
                appointment.setStatus(AppointmentStatus.CANCELLED);
                System.out.println("Appointment " + appointmentId + " cancelled by student " + student.getName());
                return true;
            }
        }
        return false;
    }

    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newDateTime, Admin admin) {
        Appointment appointment = findAppointment(appointmentId);
        if (appointment != null) {
            return admin.rescheduleAppointment(appointment, newDateTime);
        }
        return false;
    }

    private Appointment findAppointment(String appointmentId) {
        return appointments.stream()
                .filter(appt -> appt.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    // Statistics for admins
    public String getAppointmentStatistics(Admin admin) {
        return admin.getAppointmentStatistics(appointments);
    }

    public List<Appointment> getAppointmentsByCategory(Admin admin, Category category) {
        return admin.getAppointmentsByCategory(appointments, category);
    }

    // Common methods
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Admin> getAllAdmins() {
        return new ArrayList<>(admins.values());
    }

    public Appointment requestAppointment(Student student, Category category, LocalDateTime dateTime) {
        if (student == null || !students.containsKey(student.getStudentId())) {
            System.out.println("Error: Can`t schedule appointment for unknown student!");
            return null;
        }
        Appointment newAppointment = new Appointment(student, category, dateTime);
        appointments.add(newAppointment);
        student.addAppointment(newAppointment);
        System.out.println("New appointment scheduled successfully: " + newAppointment.getAppointmentId());
        return newAppointment;
    }

    public List<Appointment> checkReminders(Student student) {
        List<Appointment> reminders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderCutoff = now.plusHours(24);

        if (student != null) {
            for (Appointment appt : student.getAppointments()) {
                if (appt.getStatus() == AppointmentStatus.SCHEDULED &&
                    appt.getDateTime().isAfter(now) &&
                    appt.getDateTime().isBefore(reminderCutoff)) {
                    reminders.add(appt);
                }
            }
        }
        reminders.sort((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()));
        return reminders;
    }
} 