// code written by adaelton Goulart and Narcis Stoica
package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUI {
    private CampusAssistSystem system;
    private Scanner scanner;
    private Object currentUser;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsoleUI() {
        this.system = new CampusAssistSystem();
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    public void run() {
        System.out.println("Welcome to CampusAssist!");

        while (true) {
            if (currentUser == null) {
                handleLogin();
                if (currentUser == null) {
                    System.out.println("Exiting CampusAssist. Goodbye!");
                    break;
                }
            } else {
                if (currentUser instanceof Student) {
                    displayReminders();
                    displayStudentMenu();
                    handleStudentChoice();
                } else if (currentUser instanceof Admin) {
                    displayAdminMenu();
                    handleAdminChoice();
                }
            }
            System.out.println("\n-------------------------------------");
        }
        scanner.close();
    }

    private void handleLogin() {
        while (currentUser == null) { // Loop until login is successful or user exits
            System.out.println("\nPlease log in.");
            System.out.print("Enter your ID (S#### for Student, A#### for Admin) or type 'exit': ");
            String userId = scanner.nextLine();

            if (userId.equalsIgnoreCase("exit")) {
                // currentUser remains null, the outer loop in run() will handle the exit.
                return;
            }

            currentUser = system.login(userId);

            if (currentUser != null) {
                // Successful login - print welcome message
                String name = (currentUser instanceof Student) ?
                             ((Student)currentUser).getName() :
                             ((Admin)currentUser).getName();
                String role = (currentUser instanceof Student) ? "Student" : "Admin";
                System.out.println("\nLogin successful! Welcome, " + name + " (" + role + ")");
                // Loop condition (currentUser == null) is now false, loop will terminate.
            } else {
                System.out.println("Login failed. ID not found. Please try again.");
                // currentUser is still null, loop will continue.
            }
        }
    }

    private void displayStudentMenu() {
        System.out.println("\n--- Student Menu ---");
        System.out.println("1. Request Support Session");
        System.out.println("2. View My Appointments");
        System.out.println("3. Provide Feedback");
        System.out.println("4. Logout");
        System.out.println("0. Exit Application");
        System.out.print("Enter your choice: ");
    }

    private void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Manage Appointments");
        System.out.println("2. View Appointment Feedback");
        System.out.println("3. Generate Feedback Report");
        System.out.println("4. Logout");
        System.out.println("0. Exit Application");
        System.out.print("Enter your choice: ");
    }

    private void displayManageAppointmentsMenu() {
        System.out.println("\n--- Manage Appointments Menu ---");
        System.out.println("1. View All Appointments");
        System.out.println("2. Approve Pending Appointments");
        System.out.println("3. Reschedule Appointment");
        System.out.println("4. Cancel Appointment");
        System.out.println("0. Return to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private void handleStudentChoice() {
        int choice = getUserChoice();
        Student student = (Student) currentUser;

        switch (choice) {
            case 1:
                handleRequestAppointment(student);
                break;
            case 2:
                handleViewAppointments(student);
                break;
            case 3:
                handleProvideFeedback(student);
                break;
            case 4:
                System.out.println("Logging out...");
                currentUser = null;
                break;
            case 0:
                System.out.println("Exiting CampusAssist. Goodbye!");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleAdminChoice() {
        int choice = getUserChoice();
        Admin admin = (Admin) currentUser;

        switch (choice) {
            case 1:
                handleManageAppointments(admin);
                break;
            case 2:
                handleViewFeedback();
                break;
            case 3:
                handleGenerateFeedbackReport();
                break;
            case 4:
                System.out.println("Logging out...");
                currentUser = null;
                break;
            case 0:
                System.out.println("Exiting CampusAssist. Goodbye!");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleManageAppointments(Admin admin) {
        while (true) {
            displayManageAppointmentsMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleViewAllAppointments();
                    break;
                case 2:
                    handleApproveAppointment(admin);
                    break;
                case 3:
                    handleRescheduleAppointment(admin);
                    break;
                case 4:
                    handleCancelAppointment(admin);
                    break;
                case 0:
                    return; 
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\n-------------------------------------");
        }
    }

    private void handleRequestAppointment(Student student) {
        System.out.println("\n--- Request Support Session ---");
        if (student == null) return; // Should not happen 

        // Select Category
        System.out.println("Select a category:");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter category number: ");
        int categoryChoice = getUserChoice();
        if (categoryChoice < 1 || categoryChoice > categories.length) {
            System.out.println("Invalid category choice.");
            return;
        }
        Category selectedCategory = categories[categoryChoice - 1];

        // Enter Date and Time
        LocalDateTime selectedDateTime = null;
        while (selectedDateTime == null) {
            System.out.print("Enter desired date and time (YYYY-MM-DD HH:mm): ");
            String dateTimeString = scanner.nextLine();
            try {
                selectedDateTime = LocalDateTime.parse(dateTimeString, formatter);
                if (selectedDateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("Cannot schedule an appointment in the past. Please enter a future date/time.");
                    selectedDateTime = null; // Reset to loop again
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:mm.");
            }
        }

        // Call system to request the appointment
        Appointment newAppointment = system.requestAppointment(student, selectedCategory, selectedDateTime);

        if (newAppointment != null) {
            System.out.println("Appointment requested successfully!");
            System.out.println("Details: " + newAppointment); // Uses Appointment's toString
        } else {
            System.out.println("Failed to request appointment. Please try again later.");
        }
    }

    private void handleViewAppointments(Student student) {
        System.out.println("\n--- View My Appointments ---");
        if (student == null) return;

        List<Appointment> upcomingAppointments = student.getUpcomingAppointments();
        List<Appointment> pastAppointments = student.getPastAppointments();

        System.out.println("\nUpcoming Appointments:");
        if (upcomingAppointments.isEmpty()) {
            System.out.println("  No upcoming appointments scheduled.");
        } else {
            for (int i = 0; i < upcomingAppointments.size(); i++) {
                Appointment appt = upcomingAppointments.get(i);
                System.out.printf("  %d. Category: %s, Date/Time: %s, Status: %s%n",
                        (i + 1),
                        appt.getCategory(),
                        appt.getDateTime().format(formatter),
                        appt.getStatus());
            }
        }

        System.out.println("\nPast Appointments:");
        if (pastAppointments.isEmpty()) {
            System.out.println("  No past appointments found.");
        } else {
            for (int i = 0; i < pastAppointments.size(); i++) {
                 Appointment appt = pastAppointments.get(i);
                 String feedbackStatus = (appt.getFeedback() != null) ? "Feedback Provided" : "No Feedback Yet";
                 System.out.printf("  %d. Category: %s, Date/Time: %s, Status: %s, Feedback: %s%n",
                         (i + 1),
                         appt.getCategory(),
                         appt.getDateTime().format(formatter),
                         appt.getStatus(),
                         (appt.getStatus() == AppointmentStatus.COMPLETED ? feedbackStatus : "N/A")); // Show feedback status only if completed
            }
        }
    }

    private void handleProvideFeedback(Student student) {
         System.out.println("\n--- Provide Feedback for Completed Sessions ---");
         if (student == null) return;

         // Get completed appointments that don't have feedback yet
         List<Appointment> completedAppointments = student.getPastAppointments().stream()
             .filter(appt -> appt.getStatus() == AppointmentStatus.COMPLETED && appt.getFeedback() == null)
             .collect(Collectors.toList()); // Need import java.util.stream.Collectors;

         if (completedAppointments.isEmpty()) {
             System.out.println("You have no completed sessions awaiting feedback.");
             return;
         }

         // Display eligible appointments
         System.out.println("Select a completed session to provide feedback for:");
         for (int i = 0; i < completedAppointments.size(); i++) {
             Appointment appt = completedAppointments.get(i);
             System.out.printf("  %d. Category: %s, Date/Time: %s%n",
                     (i + 1),
                     appt.getCategory(),
                     appt.getDateTime().format(formatter));
         }
         System.out.print("Enter session number (or 0 to cancel): ");

         int sessionChoice = getUserChoice();
         if (sessionChoice <= 0 || sessionChoice > completedAppointments.size()) {
             System.out.println("Feedback cancelled.");
             return;
         }

         Appointment selectedAppointment = completedAppointments.get(sessionChoice - 1);

         // Get rating
         int rating = -1;
         while (rating < 1 || rating > 5) {
             System.out.print("Enter rating (1-5, where 5 is best): ");
             rating = getUserChoice();
             if (rating < 1 || rating > 5) {
                 System.out.println("Invalid rating. Please enter a number between 1 and 5.");
             }
         }

         // Get comments
         System.out.print("Enter comments (optional, press Enter to skip): ");
         String comments = scanner.nextLine();

         // Submit feedback via Student object
         student.provideFeedback(selectedAppointment, rating, comments);

         // Confirmation is printed within the Student.provideFeedback method currently
    }

    private void handleViewAllAppointments() {
        System.out.println("\n--- All Appointments ---");
        List<Appointment> appointments = system.getAllAppointments();
        displayAppointmentList(appointments);
    }

    private void handleApproveAppointment(Admin admin) {
        System.out.println("\n--- Approve Appointment ---");
        List<Appointment> pendingAppointments = system.getAllAppointments().stream()
            .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED)
            .collect(Collectors.toList());
        
        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments to approve.");
            return;
        }

        displayAppointmentList(pendingAppointments);
        System.out.print("Enter appointment number to approve (0 to cancel): ");
        int choice = getUserChoice();
        
        if (choice > 0 && choice <= pendingAppointments.size()) {
            Appointment appointment = pendingAppointments.get(choice - 1);
            if (system.approveAppointment(appointment.getAppointmentId(), admin)) {
                System.out.println("Appointment approved successfully.");
            } else {
                System.out.println("Failed to approve appointment.");
            }
        }
    }

    private void handleCancelAppointment(Admin admin) {
        System.out.println("\n--- Cancel Appointment ---");
        List<Appointment> activeAppointments = system.getAllAppointments().stream()
            .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
            .collect(Collectors.toList());
        
        if (activeAppointments.isEmpty()) {
            System.out.println("No active appointments to cancel.");
            return;
        }

        displayAppointmentList(activeAppointments);
        System.out.print("Enter appointment number to cancel (0 to cancel): ");
        int choice = getUserChoice();
        
        if (choice > 0 && choice <= activeAppointments.size()) {
            Appointment appointment = activeAppointments.get(choice - 1);
            if (system.cancelAppointment(appointment.getAppointmentId(), admin)) {
                System.out.println("Appointment cancelled successfully.");
            } else {
                System.out.println("Failed to cancel appointment.");
            }
        }
    }

    private void handleRescheduleAppointment(Admin admin) {
        System.out.println("\n--- Reschedule Appointment ---");
        List<Appointment> activeAppointments = system.getAllAppointments().stream()
            .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
            .collect(Collectors.toList());
        
        if (activeAppointments.isEmpty()) {
            System.out.println("No active appointments to reschedule.");
            return;
        }

        displayAppointmentList(activeAppointments);
        System.out.print("Enter appointment number to reschedule (0 to cancel): ");
        int choice = getUserChoice();
        
        if (choice > 0 && choice <= activeAppointments.size()) {
            Appointment appointment = activeAppointments.get(choice - 1);
            
            LocalDateTime newDateTime = null;
            while (newDateTime == null) {
                System.out.print("Enter new date and time (YYYY-MM-DD HH:mm): ");
                String dateTimeString = scanner.nextLine();
                try {
                    newDateTime = LocalDateTime.parse(dateTimeString, formatter);
                    if (newDateTime.isBefore(LocalDateTime.now())) {
                        System.out.println("Cannot reschedule to a past date/time.");
                        newDateTime = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:mm");
                }
            }
            
            if (system.rescheduleAppointment(appointment.getAppointmentId(), newDateTime, admin)) {
                System.out.println("Appointment rescheduled successfully.");
            } else {
                System.out.println("Failed to reschedule appointment.");
            }
        }
    }

    private void handleViewFeedback() {
        System.out.println("\n--- View Appointment Feedback ---");
        List<Appointment> completedAppointments = system.getAllAppointments().stream()
            .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED && a.getFeedback() != null)
            .collect(Collectors.toList());

        if (completedAppointments.isEmpty()) {
            System.out.println("No appointments with feedback found.");
            return;
        }

        System.out.println("\nAppointments with Feedback:");
        for (int i = 0; i < completedAppointments.size(); i++) {
            Appointment appt = completedAppointments.get(i);
            Feedback feedback = appt.getFeedback();
            System.out.printf("\n%d. Appointment Details:%n", (i + 1));
            System.out.printf("   Student: %s%n", appt.getStudent().getName());
            System.out.printf("   Category: %s%n", appt.getCategory());
            System.out.printf("   Date/Time: %s%n", appt.getDateTime().format(formatter));
            System.out.printf("   Rating: %d/5%n", feedback.getRating());
            System.out.printf("   Comments: %s%n", feedback.getComments().isEmpty() ? "No comments provided" : feedback.getComments());
        }
    }

    private void handleGenerateFeedbackReport() {
        System.out.println("\n--- Feedback Analysis Report ---");
        
        List<Appointment> completedAppointments = system.getAllAppointments().stream()
            .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
            .collect(Collectors.toList());

        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments found for analysis.");
            return;
        }

        // Calculate statistics by category
        Map<Category, List<Appointment>> appointmentsByCategory = completedAppointments.stream()
            .collect(Collectors.groupingBy(Appointment::getCategory));

        System.out.println("\nFeedback Analysis by Category:");
        appointmentsByCategory.forEach((category, appointments) -> {
            long totalWithFeedback = appointments.stream()
                .filter(a -> a.getFeedback() != null)
                .count();
            
            if (totalWithFeedback > 0) {
                double avgRating = appointments.stream()
                    .filter(a -> a.getFeedback() != null)
                    .mapToInt(a -> a.getFeedback().getRating())
                    .average()
                    .orElse(0.0);

                System.out.printf("\n%s:%n", category);
                System.out.printf("  Total Sessions: %d%n", appointments.size());
                System.out.printf("  Sessions with Feedback: %d%n", totalWithFeedback);
                System.out.printf("  Average Rating: %.2f/5.00%n", avgRating);
                
                // Show recent comments
                System.out.println("  Recent Comments:");
                appointments.stream()
                    .filter(a -> a.getFeedback() != null && !a.getFeedback().getComments().isEmpty())
                    .limit(3)
                    .forEach(a -> System.out.printf("   - %s%n", a.getFeedback().getComments()));
            }
        });

        // Overall statistics
        long totalWithFeedback = completedAppointments.stream()
            .filter(a -> a.getFeedback() != null)
            .count();
        
        if (totalWithFeedback > 0) {
            double overallAvgRating = completedAppointments.stream()
                .filter(a -> a.getFeedback() != null)
                .mapToInt(a -> a.getFeedback().getRating())
                .average()
                .orElse(0.0);

            System.out.println("\nOverall Statistics:");
            System.out.printf("Total Completed Sessions: %d%n", completedAppointments.size());
            System.out.printf("Sessions with Feedback: %d (%.1f%%)%n", 
                totalWithFeedback, 
                (double)totalWithFeedback/completedAppointments.size() * 100);
            System.out.printf("Overall Average Rating: %.2f/5.00%n", overallAvgRating);
        }
    }

    private void displayAppointmentList(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appt = appointments.get(i);
            String feedbackStatus = appt.getFeedback() != null ? 
                                  String.format(" [Feedback: %d/5]", appt.getFeedback().getRating()) : 
                                  " [No Feedback]";
            System.out.printf("%d. Student: %s, Category: %s, Date/Time: %s, Status: %s%s%n",
                (i + 1),
                appt.getStudent().getName(),
                appt.getCategory(),
                appt.getDateTime().format(formatter),
                appt.getStatus(),
                appt.getStatus() == AppointmentStatus.COMPLETED ? feedbackStatus : "");
        }
    }

    private void displayReminders() {
        if (currentUser instanceof Student) {
            Student student = (Student) currentUser;
            List<Appointment> reminders = system.checkReminders(student);
            if (!reminders.isEmpty()) {
                System.out.println("\n=== Upcoming Appointment Reminders ===");
                for (Appointment appt : reminders) {
                    System.out.printf("Reminder: You have a %s appointment on %s%n",
                        appt.getCategory(),
                        appt.getDateTime().format(formatter));
                }
                System.out.println("=====================================");
            }
        }
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new ConsoleUI().run();
    }
} 