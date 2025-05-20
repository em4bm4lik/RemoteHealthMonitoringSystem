package usermanagement;

import appointmentscheduling.*;
import chatandvideoconsultation.ChatClient;
import chatandvideoconsultation.VideoCall;
import emergencyalertsystem.EmergencyAlert;
import emergencyalertsystem.PanicButton;
import filedatahandling.Database;
import healthdatahandling.ReportGenerator;
import healthdatahandling.VitalsDatabase;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import userpatientinteraction.Consultation;
import userpatientinteraction.MedicalHistory;
import userpatientinteraction.Prescription;
import utilities.GUIHandler;
import utilities.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Patient extends User {
    private LocalDate lastVitalsSubmissionDate;
    private HashMap<Integer, Appointment> appointments = new HashMap<>();
    private HashMap<Integer, Doctor> doctors = new HashMap<>();
    private MedicalHistory medicalHistory = new MedicalHistory();
    private PanicButton panicButton = new PanicButton();
    private ArrayList<String> notifications = new ArrayList<>();

    public Patient(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        Administrator.addPatient(this); // Adding patient to system
        Administrator.addLog("Patient Registration", this + " registered in system patients"); // Logging registration
    }

    public void getOptions() {
        LocalDate todayDate = LocalDate.now();

        if (lastVitalsSubmissionDate != null && !lastVitalsSubmissionDate.equals(todayDate)) {
            int doctorId = 0;
            boolean takingPrescriptions = false;
            for (Consultation c : getMedicalHistory().getConsultations()) {
                for (Prescription p : c.getFeedback().getPrescriptions()) {
                    if (p.getStartDate().isBefore(todayDate) &&
                            p.getEndDate().isAfter(todayDate)) {
                        takingPrescriptions = true;
                        doctorId = c.getDoctor().getId();
                        break;
                    }
                }
                if (takingPrescriptions) break;
            }
            if (takingPrescriptions) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Vitals Submission Required");
                alert.setHeaderText(
                        "You have to submit vitals everyday you log in while you are taking any prescription.\n" +
                                "This submission will help us track the effects of our prescription on your health."
                );
                alert.getDialogPane().getButtonTypes().setAll(
                        new ButtonType("Submit Vitals", ButtonBar.ButtonData.OK_DONE)
                );

                alert.showAndWait();

                submitVitals(doctorId);
            }

        }

        Stage stage = new Stage();
        stage.setTitle("Patient Menu: " + getName());
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button b1  = new Button("List All Appointments");
        Button b2  = new Button("List All Doctors");
        Button b3  = new Button("Request an Appointment");
        Button b4  = new Button("View Your Medical History");
        Button b5  = new Button("History with a Particular Doctor");
        Button b6  = new Button("History for a Particular Date");
        Button b7  = new Button("Press Panic Button");
        Button b8  = new Button("View SMS Notifications");
        Button b9  = new Button("Show All Chats");
        Button b10 = new Button("Chat with a Doctor");
        Button b11 = new Button("Video Call a Doctor");
        Button b12 = new Button("View Health Trend Graph");
        Button b13 = new Button("Generate Your Report");
        Button back = new Button("Go Back");

        b1.setOnAction(e -> { listAppointments(); });
        b2.setOnAction(e -> { listAllDoctors(); });
        b3.setOnAction(e -> { requestAppointment(); });
        b4.setOnAction(e -> { viewCompleteHistory(); });
        b5.setOnAction(e -> { viewHistoryByDoctor(); });
        b6.setOnAction(e -> { viewHistoryByDate(); });
        b7.setOnAction(e -> { getPanicButton().press(this); });
        b8.setOnAction(e -> {
            GUIHandler.show(
                    "Your SMS Notifications:\n" +
                            Utilities.collectionToString(notifications, "SMS Notification")
            );
        });
        b9.setOnAction(e -> { showAllChats(); });
        b10.setOnAction(e -> { chatWithDoctor(); });
        b11.setOnAction(e -> { videoCallDoctor(); });
        b12.setOnAction(e -> {
            VitalsDatabase.plotChart(
                    VitalsDatabase.generateHealthTrendGraph(getId())
            );
        });
        b13.setOnAction(e -> { ReportGenerator.generateReport(this); });
        back.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13, back
        );

        stage.setScene(new Scene(root, 300, 600));
        stage.showAndWait();
    }

    private void listAppointments() {

        GUIHandler.show(
                "Your Appointments:\n\n" +
                        Utilities.collectionToString(appointments.values(), "Appointment")
        );


        Stage stage = new Stage();
        stage.setTitle("Appointments Menu");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button cancelReqBtn    = new Button("Request Cancellation");
        Button viewFeedbackBtn = new Button("View Doctor's Feedback");
        Button removeCancBtn   = new Button("Remove Cancelled");
        Button removeCompBtn   = new Button("Remove Completed");
        Button backBtn         = new Button("Go Back");

        cancelReqBtn.setOnAction(e -> {
            requestAppointmentCancellation();
        });
        viewFeedbackBtn.setOnAction(e -> {
            viewFeedback();
        });
        removeCancBtn.setOnAction(e -> {
            for (Appointment appt : appointments.values()) {
                if (appt.getStatus().equals("Cancelled")) {
                    removeAppointment(appt);
                }
            }
            GUIHandler.show("Cancelled appointments removed!");
        });
        removeCompBtn.setOnAction(e -> {
            for (Appointment appt : appointments.values()) {
                if (appt.getStatus().equals("Complete")) {
                    removeAppointment(appt);
                }
            }
            GUIHandler.show("Completed appointments removed!");
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                cancelReqBtn,
                viewFeedbackBtn,
                removeCancBtn,
                removeCompBtn,
                backBtn
        );

        stage.setScene(new Scene(root, 300, 250));
        stage.showAndWait();
    }

    private void viewFeedback() {
        int appointmentId = getValidAppointmentId(); // Asking for valid appointment ID
        if (appointmentId == 0) return;

        // Showing feedback based on appointment status
        if (appointments.get(appointmentId).getStatus().equals("Complete"))
            GUIHandler.show(appointments.get(appointmentId).getFeedback().toString());
        else if (appointments.get(appointmentId).getStatus().equals("Pending"))
            GUIHandler.show("The appointment is still pending. No feedback yet!");
        else
            GUIHandler.show("This appointment has been cancelled! Check appointment status");
    }

    private void requestAppointmentCancellation() {
        int appointmentId = getValidAppointmentId(); // Asking for valid appointment ID
        if (appointmentId == 0) return;

        // Finalizing cancellation if appointment is still pending
        if (appointments.get(appointmentId).getStatus().equals("Pending")) {
            AppointmentManager.finalizeCancellation(appointments.get(appointmentId));
            Administrator.addLog("Appointment Cancellation Request",
                    this + " requested the cancellation of appointment " + appointments.get(appointmentId).toString());
        } else
            GUIHandler.show("This appointment is already not pending. Check appointment status");
    }

    private void removeAppointment(Appointment appointment) {
        removeDoctor(appointment.getDoctor().getId()); // Removing doctor if no other appointment remains
        appointments.remove(appointment.getId()); // Removing appointment from record
        GUIHandler.show("Your appointment " + appointment + " has been removed");
    }

    private void removeDoctor(int doctorId) {
        int count = 0;
        for (Appointment appointment : appointments.values())
            if (appointment.getDoctor().getId() == doctorId)
                count++;
        if (count == 1)
            doctors.remove(doctorId); // Removing doctor if only one appointment existed
    }

    private void listAllDoctors() {
        GUIHandler.show(
                "Available Doctors:\n\n" +
                        Utilities.collectionToString(Database.getDoctors().values(), "Doctor")
        );

        Stage stage = new Stage();
        stage.setTitle("Doctors Menu");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button requestBtn = new Button("Request an Appointment");
        Button chatBtn    = new Button("Chat with a Doctor");
        Button backBtn    = new Button("Go Back");

        requestBtn.setOnAction(e -> {
            requestAppointment();
        });
        chatBtn.setOnAction(e -> {
            chatWithDoctor();
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(requestBtn, chatBtn, backBtn);

        stage.setScene(new Scene(root, 300, 200));
        stage.showAndWait();
    }

    private void requestAppointment() {
        int doctorId;
        // Prompt until we get a valid ID or 0
        do {
            String s = GUIHandler.prompt("Enter the ID of the doctor (or 0 to go back):");
            doctorId = Integer.parseInt(s);
            if (doctorId == 0) return;
            if (!Database.getDoctors().containsKey(doctorId))
                GUIHandler.show("There are no doctors with ID: " + doctorId);
        } while (!Database.getDoctors().containsKey(doctorId));

        AppointmentManager.requestFromDoctor(doctorId, this);

        GUIHandler.show("Please submit your vitals to the doctor.");
        submitVitals(doctorId);

        Administrator.addLog("Appointment Request and Vitals Submission",
                this + " requested an appointment from doctor ID: " + doctorId +
                        " and submitted his vitals for this appointment"
        );
        GUIHandler.show("Appointment requested! If approved, it will automatically be added to your appointments");
    }

    private void submitVitals(int doctorId) {
        float heartRate = getValidatedVital("Heart Rate (BPM)", 30f, 200f);
        if (heartRate < 60f || heartRate > 100f)
            new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Heart Rate out of threshold!").trigger();

        float bloodPressure = getValidatedVital("Blood Pressure (Systolic mmHg)", 70f, 250f);
        if (bloodPressure < 90f || bloodPressure > 140f)
            new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Blood Pressure out of threshold!").trigger();

        float oxygenLevel = getValidatedVital("Oxygen Level (%)", 50f, 100f);
        if (oxygenLevel < 95f || oxygenLevel > 100f)
            new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Oxygen Level out of threshold!").trigger();

        float temperature = getValidatedVital("Temperature (Fahrenheit)", 90f, 110f);
        if (temperature < 97f || temperature > 99.5f)
            new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Temperature out of threshold!").trigger();

        VitalsDatabase.addPatientVitals(getId(), heartRate, bloodPressure, oxygenLevel, temperature);
        lastVitalsSubmissionDate = LocalDate.now();
    }

    private float getValidatedVital(String promptText, float minVal, float maxVal) {
        float value;
        do {
            String s = GUIHandler.prompt(
                    "Enter your " + promptText +
                            " { " + minVal + " - " + maxVal + " }:"
            );
            value = Float.parseFloat(s);
            if (value < minVal || value > maxVal) {
                GUIHandler.show(
                        "Invalid Value. " + promptText +
                                " must be between " + minVal + " and " + maxVal + "."
                );
            }
        } while (value < minVal || value > maxVal);
        return value;
    }

    private void viewCompleteHistory() {
        GUIHandler.show(medicalHistory.toString());
    }

    private void viewHistoryByDoctor() {
        String s = GUIHandler.prompt("Enter Doctor ID:");
        int doctorId = Integer.parseInt(s);

        StringBuilder builder = new StringBuilder("Your history with the specified doctor:\n\n");
        for (int i = 0; i < getMedicalHistory().getConsultations(doctorId).size(); i++) {
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(":\n")
                    .append(getMedicalHistory().getConsultations(doctorId).get(i))
                    .append("\n\n");
        }
        GUIHandler.show(builder.toString());
    }

    private void viewHistoryByDate() {
        String input = GUIHandler.prompt("Enter Date (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        StringBuilder builder = new StringBuilder("Patient's history on specified date:\n\n");
        for (int i = 0; i < getMedicalHistory().getConsultations(date).size(); i++) {
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(":\n")
                    .append(getMedicalHistory().getConsultations(date).get(i))
                    .append("\n\n");
        }
        GUIHandler.show(builder.toString());
    }

    private void showAllChats() {
        ChatClient.showChatList(this);

        Stage stage = new Stage();
        stage.setTitle("Chat Menu");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button chatBtn = new Button("Chat with a Doctor");
        Button backBtn = new Button("Go Back");

        chatBtn.setOnAction(e -> {
            chatWithDoctor();
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(chatBtn, backBtn);

        stage.setScene(new Scene(root, 250, 150));
        stage.showAndWait();
    }

    private void chatWithDoctor() {
        // Prompt for doctor ID
        int doctorId = getValidDoctorId(Database.getDoctors());
        if (doctorId == 0) return;
        if (!Database.getDoctors().containsKey(doctorId)) {
            GUIHandler.show("There are no doctors with ID: " + doctorId);
            return;
        }
        ChatClient.showChat(this, Database.getDoctors().get(doctorId));
    }

    private void videoCallDoctor() {
        // Prompt for doctor ID
        int doctorId = getValidDoctorId(Database.getDoctors());
        if (doctorId == 0) return;
        if (!Database.getDoctors().containsKey(doctorId)) {
            GUIHandler.show("There are no doctors with ID: " + doctorId);
            return;
        }
        GUIHandler.show("Here's your video call link: " +
                VideoCall.generateLink(this, Database.getDoctors().get(doctorId))
        );
    }

    public void addNewAppointment(Appointment newAppointment) {
        appointments.put(newAppointment.getId(), newAppointment);
        addNewDoctor(newAppointment.getDoctor());
        Administrator.addLog("Appointment and Doctor addition to Patient",
                "Appointment Manager added " + newAppointment +
                        " and " + newAppointment.getDoctor() +
                        " to " + this + "'s appointments and doctors"
        );
    }

    private void addNewDoctor(Doctor newDoctor) {
        doctors.put(newDoctor.getId(), newDoctor); // Adding doctor to patient records
    }

    public void addNewSMSNotification(String newNotification) {
        notifications.add(0, newNotification);
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory; // Returning medical history object
    }

    public PanicButton getPanicButton() {
        return panicButton; // Returning panic button object
    }

    public HashMap<Integer, Doctor> getDoctors() {
        return doctors; // Returning doctors hashmap
    }

    public HashMap<Integer, Appointment> getAppointments() {
        return appointments;
    }

    private int getValidDoctorId(HashMap<Integer, Doctor> lookupFrom) {
        int doctorId;
        do {
            String s = GUIHandler.prompt("Enter the ID of the doctor (or 0 to go back):");
            doctorId = Integer.parseInt(s);
            if (doctorId == 0) return 0;
            if (!lookupFrom.containsKey(doctorId)) {
                GUIHandler.show("There are no doctors with ID: " + doctorId);
            }
        } while (!lookupFrom.containsKey(doctorId));
        return doctorId;
    }

    private int getValidAppointmentId() {
        int appointmentId;
        do {
            String s = GUIHandler.prompt("Enter Appointment ID (or 0 to go back):");
            appointmentId = Integer.parseInt(s);
            if (appointmentId == 0) return 0;
            if (!appointments.containsKey(appointmentId)) {
                GUIHandler.show("No appointment with ID: " + appointmentId);
            }
        } while (!appointments.containsKey(appointmentId));
        return appointmentId;
    }

    @Override
    public String toString() {
        return "Patient ID: " + getId() + "\t\t" +
                "Patient Name: " + getName(); // Converting patient object to string
    }
}
