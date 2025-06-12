package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.*;
import java.time.LocalDateTime;

public class StudentTest {
    @Test
    void testAddAndGetAppointments() {
        Student student = new Student("S1001", "Test Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        student.addAppointment(appt);
        assertEquals(1, student.getAppointments().size());
        assertTrue(student.getAppointments().contains(appt));
    }

    @Test
    void testGetUpcomingAndPastAppointments() {
        Student student = new Student("S1002", "Test Student");
        Appointment past = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().minusDays(2));
        past.setStatus(AppointmentStatus.COMPLETED);
        Appointment upcoming = new Appointment(student, Category.MENTAL_HEALTH, LocalDateTime.now().plusDays(2));
        student.addAppointment(past);
        student.addAppointment(upcoming);
        assertEquals(1, student.getUpcomingAppointments().size());
        assertEquals(1, student.getPastAppointments().size());
    }

    @Test
    void testProvideFeedback() {
        Student student = new Student("S1003", "Test Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().minusDays(1));
        appt.setStatus(AppointmentStatus.COMPLETED);
        student.addAppointment(appt);
        Feedback feedback = student.provideFeedback(appt, 5, "Great session");
        assertNotNull(feedback);
        assertEquals(5, feedback.getRating());
        assertEquals("Great session", feedback.getComments());
        assertEquals(feedback, appt.getFeedback());
    }

    @Test
    void testProvideFeedbackForInvalidAppointment() {
        Student student = new Student("S1004", "Test Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        student.addAppointment(appt);
        // Not completed, should not allow feedback
        Feedback feedback = student.provideFeedback(appt, 4, "Not completed");
        assertNull(feedback);
    }
} 