package usermanagement;

import appointmentscheduling.Appointment;
import filedatahandling.Database;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import notificationsreminders.ReminderService;
import userpatientinteraction.Consultation;
import userpatientinteraction.Prescription;
import utilities.GUIHandler;
import utilities.Utilities;

import java.time.LocalDate;
import java.util.HashMap;


public class Administrator extends User {

    private int uploads;
    private HashMap<Integer, LogEntry> pendingLogs = new HashMap<>();
    private LocalDate lastRemindersSentDate;

    public Administrator(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        this.uploads = 0;
        Database.getAdministrators().put(this.getId(), this);
        Administrator.addLog("Administrator Registration",
                this + " registered in system administrators"
        );
    }

    public static void addLog(String logType, String details) {
        Administrator assignee = getLeastBusy();
        if (assignee != null) {
            LogEntry newLog = new LogEntry(logType, details);
            assignee.pendingLogs.put(newLog.getLogId(), newLog);
        }
    }

    public static void addDoctor(Doctor newDoctor) {
        Administrator assignee = getLeastBusy();
        if (assignee == null)
            Database.getDoctors().put(newDoctor.getId(), newDoctor);
        else
            assignee.addToDoctors(newDoctor);
    }

    public static void addPatient(Patient newPatient) {
        Administrator assignee = getLeastBusy();
        if (assignee == null)
            Database.getPatients().put(newPatient.getId(), newPatient);
        else
            assignee.addToPatients(newPatient);
    }

    public void getOptions() {
        if (lastRemindersSentDate == null || !lastRemindersSentDate.equals(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reminders Required");
            alert.setHeaderText("You need to push today's reminders to proceed to main menu");
            alert.getDialogPane().getButtonTypes().setAll(new ButtonType("Push Reminders", ButtonBar.ButtonData.OK_DONE));

            alert.showAndWait();

            sendTodayReminders();
            lastRemindersSentDate = LocalDate.now();
        }


        // Button menu for the admin options
        Stage stage = new Stage();
        stage.setTitle("Administrator Menu: " + getName());
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button pendingLogsBtn = new Button("Show Pending Logs");
        Button allLogsBtn = new Button("Show All Logs");
        Button doctorsBtn = new Button("Show All Doctors");
        Button patientsBtn = new Button("Show All Patients");
        Button adminsBtn = new Button("Show All Administrators");
        Button backBtn = new Button("Go Back");

        pendingLogsBtn.setOnAction(e -> showPendingLogs());
        allLogsBtn    .setOnAction(e -> GUIHandler.show(
                "System Logs:\n\n" +
                        Utilities.collectionToString(Database.getLogs().values(), "Log")
        ));
        doctorsBtn.setOnAction(e -> GUIHandler.show(
                "Registered Doctors:\n\n" +
                        Utilities.collectionToString(Database.getDoctors().values(), "Doctor")
        ));
        patientsBtn.setOnAction(e -> GUIHandler.show(
                "Registered Patients:\n\n" +
                        Utilities.collectionToString(Database.getPatients().values(), "Patient")
        ));
        adminsBtn.setOnAction(e -> GUIHandler.show(
                "Registered Administrators:\n\n" +
                        Utilities.collectionToString(Database.getAdministrators().values(), "Administrator")
        ));
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                pendingLogsBtn,
                allLogsBtn,
                doctorsBtn,
                patientsBtn,
                adminsBtn,
                backBtn
        );

        stage.setScene(new Scene(root, 300, 300));
        stage.showAndWait();
    }

    private void showPendingLogs() {
        GUIHandler.show(
                "Your Pending Logs:\n\n" +
                        Utilities.collectionToString(pendingLogs.values(), "Log")
        );

        Stage stage = new Stage();
        stage.setTitle("Pending Logs");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button addLogBtn = new Button("Add Log to System");
        Button backBtn   = new Button("Go Back");

        addLogBtn.setOnAction(e -> addToLogs());
        backBtn .setOnAction(e -> stage.close());

        root.getChildren().addAll(addLogBtn, backBtn);

        stage.setScene(new Scene(root, 250, 150));
        stage.showAndWait();
    }

    private void addToLogs() {
        int logId;
        do {
            String s = GUIHandler.prompt("Enter Log ID (or 0 to go back):");
            logId = Integer.parseInt(s);
            if (logId == 0) return;
            if (!pendingLogs.containsKey(logId))
                GUIHandler.show("No log with ID " + logId + " is pending");
        } while (!pendingLogs.containsKey(logId));

        Database.getLogs().put(logId, pendingLogs.remove(logId));
        setUploads(getUploads() + 1);
        System.out.println(this + " added a new Log to system Logs!");
    }

    private void addToDoctors(Doctor newDoctor) {
        Database.getDoctors().put(newDoctor.getId(), newDoctor);
        setUploads(getUploads() + 1);
        System.out.println(this + " registered a new doctor in system Doctors!");
    }

    private void addToPatients(Patient newPatient) {
        Database.getPatients().put(newPatient.getId(), newPatient);
        setUploads(getUploads() + 1);
        System.out.println(this + " registered a new patient in system Patients!");
    }

    private static Administrator getLeastBusy() {
        Administrator leastBusy = Database.getAdministrators().get(1);
        for (Administrator admin : Database.getAdministrators().values()) {
            if (admin.getUploads() < leastBusy.getUploads())
                leastBusy = admin;
        }
        return leastBusy;
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

                if (appointmentWhen != null) {
                    String message = "APPOINTMENT REMINDER!\nDear " + patient.getName() + "!\n" +
                            "You have the following appointment " + appointmentWhen + "\n" + appointment;
                    service.sendReminder(patient, message);
                }
            }

            for (Consultation consultation : patient.getMedicalHistory().getConsultations()) {
                for (Prescription prescription : consultation.getFeedback().getPrescriptions()) {
                    LocalDate start = prescription.getStartDate();
                    LocalDate oneMonthLater = start.plusMonths(1);

                    // if today is between startDate (inclusive) and oneMonthLater (inclusive)
                    if (!todayDate.isBefore(start) && !todayDate.isAfter(oneMonthLater)) {
                        String message = "MEDICATION REMINDER!\nDear " + patient.getName() + "!\n" +
                                "Remember to take the following medicine today!\n" + prescription + "\n" +
                                "WE HOPE AND PRAY THAT YOU GET WELL SOON!";
                        service.sendReminder(patient, message);
                    }
                }
            }
        }
    }

    public int getUploads() { return uploads; }

    public void setUploads(int uploads) { this.uploads = uploads; }

    @Override
    public String toString() {
        return "Admin ID: " + getId() + "\t\tName: " + getName();
    }
}

