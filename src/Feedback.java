//THIS CODE WAS WRITTEN BY ADAELTON GOUART
package src;

import java.util.UUID;

public class Feedback {
    private String feedbackId;
    private Appointment appointment; 
    private int rating;
    private String comments;

    // Constructor now takes the appointment it's for
    public Feedback(Appointment appointment, int rating, String comments) {
        this.feedbackId = UUID.randomUUID().toString(); 
        this.appointment = appointment;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters
    public String getFeedbackId() {
        return feedbackId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

  

    @Override
    public String toString() {
        return "Feedback{" +
               "id='" + feedbackId + '\'' +
                ", appointmentId='" + (appointment != null ? appointment.getAppointmentId() : "null") + '\'' +
               ", rating=" + rating +
               ", comments='" + comments + '\'' +
               '}';
    }
} 