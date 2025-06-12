package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminTest {
    @Test
    void testApproveAppointment() {
        Admin admin = new Admin("A1001", "Admin User", "admin@uni.edu", "Support Staff");
        Student student = new Student("S2001", "Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        assertTrue(admin.approveAppointment(appt));
        assertEquals(AppointmentStatus.APPROVED, appt.getStatus());
    }

    @Test
    void testCancelAppointment() {
        Admin admin = new Admin("A1002", "Admin User", "admin@uni.edu", "Support Staff");
        Student student = new Student("S2002", "Student");
        Appointment appt = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(1));
        assertTrue(admin.cancelAppointment(appt));
        assertEquals(AppointmentStatus.CANCELLED, appt.getStatus());
    }

    @Test
    void testRescheduleAppointment() {
        Admin admin = new Admin("A1003", "Admin User", "admin@uni.edu", "Support Staff");
        Student student = new Student("S2003", "Student");
        Appointment appt = new Appointment(student, Category.MENTAL_HEALTH, LocalDateTime.now().plusDays(1));
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        assertTrue(admin.rescheduleAppointment(appt, newTime));
        assertEquals(newTime, appt.getDateTime());
    }

    @Test
    void testGetAppointmentsByCategory() {
        Admin admin = new Admin("A1004", "Admin User", "admin@uni.edu", "Support Staff");
        Student student = new Student("S2004", "Student");
        Appointment appt1 = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        Appointment appt2 = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(2));
        List<Appointment> all = new ArrayList<>();
        all.add(appt1);
        all.add(appt2);
        List<Appointment> filtered = admin.getAppointmentsByCategory(all, Category.ACADEMIC_SUPPORT);
        assertEquals(1, filtered.size());
        assertEquals(Category.ACADEMIC_SUPPORT, filtered.get(0).getCategory());
    }

    @Test
    void testGetAppointmentStatistics() {
        Admin admin = new Admin("A1005", "Admin User", "admin@uni.edu", "Support Staff");
        Student student = new Student("S2005", "Student");
        List<Appointment> all = new ArrayList<>();
        Appointment appt1 = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        appt1.setStatus(AppointmentStatus.SCHEDULED);
        Appointment appt2 = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(2));
        appt2.setStatus(AppointmentStatus.APPROVED);
        Appointment appt3 = new Appointment(student, Category.MENTAL_HEALTH, LocalDateTime.now().minusDays(1));
        appt3.setStatus(AppointmentStatus.COMPLETED);
        Appointment appt4 = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().minusDays(2));
        appt4.setStatus(AppointmentStatus.CANCELLED);
        all.add(appt1); all.add(appt2); all.add(appt3); all.add(appt4);
        String stats = admin.getAppointmentStatistics(all);
        assertTrue(stats.contains("Total Appointments: 4"));
        assertTrue(stats.contains("Scheduled: 1"));
        assertTrue(stats.contains("Approved: 1"));
        assertTrue(stats.contains("Completed: 1"));
        assertTrue(stats.contains("Cancelled: 1"));
    }
} 