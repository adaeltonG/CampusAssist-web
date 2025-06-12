package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import src.*;
import java.time.LocalDateTime;

public class AppointmentTest {
    @Test
    void testAppointmentCreation() {
        Student student = new Student("S3001", "Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        assertNotNull(appt.getAppointmentId());
        assertEquals(student, appt.getStudent());
        assertEquals(Category.ACADEMIC_SUPPORT, appt.getCategory());
        assertEquals(AppointmentStatus.SCHEDULED, appt.getStatus());
        assertNull(appt.getFeedback());
    }

    @Test
    void testSetStatusAndDateTime() {
        Student student = new Student("S3002", "Student");
        Appointment appt = new Appointment(student, Category.FINANCIAL_AID, LocalDateTime.now().plusDays(1));
        appt.setStatus(AppointmentStatus.APPROVED);
        assertEquals(AppointmentStatus.APPROVED, appt.getStatus());
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        appt.setDateTime(newTime);
        assertEquals(newTime, appt.getDateTime());
    }

    @Test
    void testSetAndGetFeedback() {
        Student student = new Student("S3003", "Student");
        Appointment appt = new Appointment(student, Category.MENTAL_HEALTH, LocalDateTime.now().plusDays(1));
        Feedback feedback = new Feedback(appt, 4, "Good");
        appt.setFeedback(feedback);
        assertEquals(feedback, appt.getFeedback());
    }

    @Test
    void testToString() {
        Student student = new Student("S3004", "Student");
        Appointment appt = new Appointment(student, Category.ACADEMIC_SUPPORT, LocalDateTime.now().plusDays(1));
        String str = appt.toString();
        assertTrue(str.contains("Appointment{"));
        assertTrue(str.contains("student=Student"));
    }
} 