//THIS CODE WAS WRITTEN BY NARCIS STOICA
package src;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Admin {
    private String adminId;
    private String name;
    private String email;
    private String role;  

    public Admin(String adminId, String name, String email, String role) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // Admin-specific methods for appointment management
    public boolean approveAppointment(Appointment appointment) {
        if (appointment != null && appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setStatus(AppointmentStatus.APPROVED);
            System.out.println("Appointment " + appointment.getAppointmentId() + " approved by admin " + this.name);
            return true;
        }
        return false;
    }

    public boolean cancelAppointment(Appointment appointment) {
        if (appointment != null && appointment.getStatus() != AppointmentStatus.CANCELLED) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            System.out.println("Appointment " + appointment.getAppointmentId() + " cancelled by admin " + this.name);
            return true;
        }
        return false;
    }

    public boolean rescheduleAppointment(Appointment appointment, LocalDateTime newDateTime) {
        if (appointment != null && appointment.getStatus() != AppointmentStatus.CANCELLED) {
            appointment.setDateTime(newDateTime);
            System.out.println("Appointment " + appointment.getAppointmentId() + " rescheduled by admin " + this.name);
            return true;
        }
        return false;
    }

    // Admin power: View all appointments for a specific category
    public List<Appointment> getAppointmentsByCategory(List<Appointment> allAppointments, Category category) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : allAppointments) {
            if (appointment.getCategory() == category) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    // Admin power: Get statistics about appointments
    public String getAppointmentStatistics(List<Appointment> allAppointments) {
        int totalAppointments = allAppointments.size();
        int scheduledAppointments = 0;
        int approvedAppointments = 0;
        int completedAppointments = 0;
        int cancelledAppointments = 0;

        for (Appointment appointment : allAppointments) {
            switch (appointment.getStatus()) {
                case SCHEDULED:
                    scheduledAppointments++;
                    break;
                case APPROVED:
                    approvedAppointments++;
                    break;
                case COMPLETED:
                    completedAppointments++;
                    break;
                case CANCELLED:
                    cancelledAppointments++;
                    break;
            }
        }

        return String.format("""
            Appointment Statistics:
            Total Appointments: %d
            Scheduled: %d
            Approved: %d
            Completed: %d
            Cancelled: %d
            """, 
            totalAppointments, scheduledAppointments, approvedAppointments, 
            completedAppointments, cancelledAppointments);
    }

    @Override
    public String toString() {
        return "Admin{" +
               "adminId='" + adminId + '\'' +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               '}';
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return java.util.Objects.equals(adminId, admin.adminId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(adminId);
    }
} 