//THIS CODE WAS WRITTEN BY ADAELTON GOUART
package src;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
    private String appointmentId;
    private Student student;
    private Category category;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private Feedback feedback; // Can be null initially

    public Appointment(Student student, Category category, LocalDateTime dateTime) {
        this.appointmentId = UUID.randomUUID().toString(); 
        this.student = student;
        this.category = category;
        this.dateTime = dateTime;
        this.status = AppointmentStatus.SCHEDULED; // Default status
        this.feedback = null;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    // Setters
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
       
    }

    @Override
    public String toString() {
        return "Appointment{" +
               "id='" + appointmentId + '\'' +
               ", student=" + student.getName() + 
               ", category=" + category +
               ", dateTime=" + dateTime +
               ", status=" + status +
               ", hasFeedback=" + (feedback != null) +
               '}';
    }
} 