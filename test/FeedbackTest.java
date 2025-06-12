package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.*;
import java.time.LocalDateTime;

public class FeedbackTest {
    @Test
    void testFeedbackCreation() {
        Student student = new Student("S4001", "Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        Feedback feedback = new Feedback(appt, 5, "Excellent");
        assertNotNull(feedback.getFeedbackId());
        assertEquals(appt, feedback.getAppointment());
        assertEquals(5, feedback.getRating());
        assertEquals("Excellent", feedback.getComments());
    }

    @Test
    void testToString() {
        Student student = new Student("S4002", "Student");
        Appointment appt = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(1));
        Feedback feedback = new Feedback(appt, 3, "Average");
        String str = feedback.toString();
        assertTrue(str.contains("Feedback{"));
        assertTrue(str.contains("appointmentId='"));
    }
} 