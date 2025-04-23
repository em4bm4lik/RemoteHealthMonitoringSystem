package usermanagement;

import appointmentscheduling.Appointment;
import notificationsreminders.ReminderService;
import userpatientinteraction.Consultation;
import userpatientinteraction.Prescription;

import java.time.LocalDate;
import java.util.HashMap;


public class Administrator extends User {

    private static LocalDate lastRemindersSentDate;
    private HashMap<Integer, LogEntry> pendingLogs = new HashMap<>(); // Holds pending logs for the current admin
    private int uploads; // Tracks the number of uploads by the admin

    // Constructor to register an administrator and initialize their uploads count
    public Administrator(String name, String email, String phone) {
        super(name, email, phone); // Call the parent constructor
        this.uploads = 0; // Initialize uploads count to zero
        Database.getAdministrators().put(this.getId(), this); // Register the new administrator
        Administrator.addLog("Administrator Registration", this + " registered in system administrators"); // Add a log entry for registration
    }

    // Method to add a log entry in the system (assigns the log to the least busy admin)
    public static void addLog(String logType, String details) {
        Administrator assignee = getLeastBusy(); // Get the least busy admin to handle the log
        LogEntry newLog = new LogEntry(logType, details); // Create a new log entry
        assignee.pendingLogs.put(newLog.getLogId(), newLog); // Add the log entry to the pending logs of the assigned admin
    }

    // Method to add a new doctor to the system (assigned to the least busy admin)
    public static void addDoctor(Doctor newDoctor) {
        Administrator assignee = getLeastBusy(); // Get the least busy admin to handle the task
        assignee.addToDoctors(newDoctor); // Add the doctor to the system
    }

    // Method to add a new patient to the system (assigned to the least busy admin)
    public static void addPatient(Patient newPatient) {
        Administrator assignee = getLeastBusy(); // Get the least busy admin to handle the task
        assignee.addToPatients(newPatient); // Add the patient to the system
    }

    // Method to display the admin options menu
    public void getOptions() {
        int input;
        if (lastRemindersSentDate == null || !(lastRemindersSentDate).equals(LocalDate.now())) {
            do {
                System.out.println("You need to push today's reminders in order to proceed to main menu");
                System.out.print("Enter 1 to push reminders: ");
                input = Utilities.readInt();
                if (input != 1)
                    System.out.println("Please enter a valid value.");
            } while (input != 1);

            sendTodayReminders();
            lastRemindersSentDate = LocalDate.now();
        }


        while (true) {
            // Display menu options
            System.out.println("Enter 1 to show all pending logs");
            System.out.println("Enter 2 to show all logs");
            System.out.println("Enter 3 to show all doctors");
            System.out.println("Enter 4 to show all patients");
            System.out.println("Enter 5 to show all administrators");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            input = Utilities.readInt(); // Read user input

            // Handle user choice
            switch (input) {
                case 0:
                    return; // Exit the loop
                case 1:
                    showPendingLogs(); // Show pending logs
                    break;
                case 2:
                    System.out.println("System Logs:\n");
                    System.out.println(Utilities.collectionToString(Database.getLogs().values(), "Log")); // Display all system logs
                    break;
                case 3:
                    System.out.println("Registered Doctors:\n");
                    System.out.println(Utilities.collectionToString(Database.getDoctors().values(), "Doctor")); // Display all doctors
                    break;
                case 4:
                    System.out.println("Registered Patients:\n");
                    System.out.println(Utilities.collectionToString(Database.getPatients().values(), "Patient")); // Display all patients
                    break;
                case 5:
                    System.out.println("Registered Administrators:\n");
                    System.out.println(Utilities.collectionToString(Database.getAdministrators().values(), "Administrator")); // Display all administrators
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    // Method to display all pending logs and allow the admin to add them to system logs
    private void showPendingLogs() {
        while (true) {
            System.out.println("Your Pending Logs:\n");
            System.out.println(Utilities.collectionToString(pendingLogs.values(), "Log")); // Display all pending logs
            System.out.println("Enter 1 to add log to system");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt(); // Read user input

            // Handle user choice
            switch (input) {
                case 0:
                    return; // Exit the loop
                case 1:
                    addToLogs(); // Add selected log to system logs
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    // Method to add a selected log to the system logs
    private void addToLogs() {
        int logId;
        do {
            System.out.print("Enter Log ID (or 0 to go back): ");
            logId = Utilities.readInt(); // Read the log ID
            if (logId == 0) return; // Exit if the user enters 0
            if (!pendingLogs.containsKey(logId))
                System.out.println("No log with ID " + logId + " is pending"); // Show error if the log ID doesn't exist
        } while (!pendingLogs.containsKey(logId));

        // Move the log from pending to system logs
        Database.getLogs().put(logId, pendingLogs.get(logId));
        pendingLogs.remove(logId);
        this.setUploads(this.getUploads() + 1); // Increase the upload count
        System.out.println(this + " added a new Log to system Logs!"); // Notify admin
    }

    // Method to add a doctor to the system
    private void addToDoctors(Doctor newDoctor) {
        Database.getDoctors().put(newDoctor.getId(), newDoctor); // Add the doctor to the system
        this.setUploads(this.getUploads() + 1); // Increase the upload count
        System.out.println(this + " registered a new doctor in system Doctors!"); // Notify admin
    }

    // Method to add a patient to the system
    private void addToPatients(Patient newPatient) {
        Database.getPatients().put(newPatient.getId(), newPatient); // Add the patient to the system
        this.setUploads(this.getUploads() + 1); // Increase the upload count
        System.out.println(this + " registered a new patient in system Patients!"); // Notify admin
    }

    // Method to get the least busy administrator (based on uploads)
    private static Administrator getLeastBusy() {
        Administrator leastBusy = Database.getAdministrators().get(1); // Start by assuming the first admin is the least busy
        for (Administrator administrator : Database.getAdministrators().values()) {
            if (administrator.getUploads() < leastBusy.getUploads()) // Find the admin with the least uploads
                leastBusy = administrator;
        }
        return leastBusy; // Return the least busy admin
    }

    private void sendTodayReminders() {
        LocalDate todayDate = LocalDate.now();
        ReminderService service = new ReminderService();

        for (Patient patient : Database.getPatients().values()) {
            for (Appointment appointment : patient.getAppointments().values()) {
                LocalDate appointmentDate = appointment.getDate();
                String appointmentWhen = null;

                if (appointmentDate.equals(todayDate))
                    appointmentWhen = "today";
                else if (appointmentDate.minusDays(3).equals(todayDate))
                    appointmentWhen = "in 3 days";
                else if(appointmentDate.minusDays(7).equals(todayDate))
                    appointmentWhen = "next week";

                String message = "APPOINTMENT REMINDER!\nDear " + patient.getName() + "!\n" +
                        "You have the following appointment " + appointmentWhen + "\n" + appointment;
                service.sendReminder(patient, message);
            }

            for (Consultation consultation: patient.getMedicalHistory().getConsultations())
                if (consultation.getDate().plusMonths(1).equals(todayDate))
                    for (Prescription prescription : consultation.getFeedback().getPrescriptions())
                        if (todayDate.isAfter(prescription.getStartDate()) && todayDate.isBefore(prescription.getEndDate())) {
                            String message = "MEDICATION REMINDER!\nDear " + patient.getName() + "!\n" +
                                    "Remember to take the following medicine today!\n" + prescription + "\n" +
                                    "WE HOPE AND PRAY THAT YOU GET WELL SOON!";
                            service.sendReminder(patient, message);
                        }

        }
    }

    // Getter and setter for uploads count
    public int getUploads() {
        return uploads;
    }

    public void setUploads(int uploads) {
        this.uploads = uploads;
    }

    // Override toString to return a string representation of the administrator
    @Override
    public String toString() {
        return "Admin ID: " + getId() + "\t\t" +
                "Name: " + getName();
    }
}

