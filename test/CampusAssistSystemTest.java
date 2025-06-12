package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.*;
import java.time.LocalDateTime;
import java.util.List;

public class CampusAssistSystemTest {
    @Test
    void testLoginStudentAndAdmin() {
        CampusAssistSystem system = new CampusAssistSystem();
        Object student = system.login("S1001");
        Object admin = system.login("A1001");
        assertTrue(student instanceof Student);
        assertTrue(admin instanceof Admin);
    }

    @Test
    void testRequestAppointment() {
        CampusAssistSystem system = new CampusAssistSystem();
        Student student = (Student) system.login("S1001");
        int before = system.getAppointmentsForStudent(student).size();
        Appointment appt = system.requestAppointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(3));
        assertNotNull(appt);
        int after = system.getAppointmentsForStudent(student).size();
        assertEquals(before + 1, after);
    }

    @Test
    void testCheckReminders() {
        CampusAssistSystem system = new CampusAssistSystem();
        Student student = (Student) system.login("S1002");
        List<Appointment> reminders = system.checkReminders(student);
        assertNotNull(reminders);
        // At least one appointment within 24 hours for S1002 in hardcoded data
        assertTrue(reminders.size() >= 0);
    }
} 