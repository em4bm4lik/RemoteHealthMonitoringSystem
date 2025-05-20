package usermanagement;

import appointmentscheduling.*;
import chatandvideoconsultation.ChatClient;
import chatandvideoconsultation.VideoCall;
import filedatahandling.Database;
import healthdatahandling.ReportGenerator;
import healthdatahandling.VitalsDatabase;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import userpatientinteraction.*;
import utilities.GUIHandler;
import utilities.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Doctor extends User {
    private HashMap<Integer, Appointment> appointmentRequests = new HashMap<>();
    private HashMap<Integer, Appointment> appointments = new HashMap<>();
    private HashMap<Integer, Patient> patients = new HashMap<>();
    private ArrayList<String> notifications = new ArrayList<>();

    public Doctor(String email, String password, String name, String phone) {
        super(email, password, name, phone);
        Administrator.addDoctor(this);
        Administrator.addLog("Doctor Registration", this + " registered in system doctors");
    }

    public void getOptions() {
        // Building a new window
        Stage stage = new Stage();
        stage.setTitle("Doctor Menu: Dr. " + getName());
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        // Creating buttons for each choice
        Button b0 = new Button("Go Back");
        Button b1 = new Button("List All Patients");
        Button b2 = new Button("List All Appointments");
        Button b3 = new Button("List Appointment Requests");
        Button b4 = new Button("List Today's Appointments");
        Button b5 = new Button("View SMS Notifications");
        Button b6 = new Button("Show All Chats");
        Button b7 = new Button("Chat with a Patient");
        Button b8 = new Button("Video Call a Patient");

        // Wiring them to existing methods
        b0.setOnAction(e -> stage.close());
        b1.setOnAction(e -> listPatients());
        b2.setOnAction(e -> listAppointments());
        b3.setOnAction(e -> listAppointmentRequests());
        b4.setOnAction(e -> listAppointmentsToday());
        b5.setOnAction(e -> {
            GUIHandler.show(
                    "Your SMS Notifications:\n" +
                            Utilities.collectionToString(notifications, "SMS Notification")
            );
        });
        b6.setOnAction(e -> showAllChats());
        b7.setOnAction(e -> chatWithPatient());
        b8.setOnAction(e -> videoCallPatient());

        // Adding them to the layout
        root.getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, b8, b0);

        // Showing the window and waiting
        stage.setScene(new Scene(root, 300, 400));
        stage.showAndWait();
    }


    private void listPatients() {
        Stage stage = new Stage();
        stage.setTitle("Your Patients");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label header = new Label("Your Patients:");
        TextArea data = new TextArea(Utilities.collectionToString(patients.values(), "Patient"));
        data.setEditable(false);
        data.setWrapText(true);

        Button viewData = new Button("View Patient Data");
        Button chat     = new Button("Chat with a Patient");
        Button back     = new Button("Go Back");

        viewData.setOnAction(e -> viewPatientData(patients));
        chat.setOnAction(e -> chatWithPatient());
        back.setOnAction(e -> stage.close());

        root.getChildren().addAll(header, data, viewData, chat, back);
        stage.setScene(new Scene(root, 400, 300));
        stage.showAndWait();
    }


    private void viewPatientData(HashMap<Integer, Patient> patients) {
        int patientId;
        do {
            String s = GUIHandler.prompt("Enter Patient ID (or 0 to go back):");
            patientId = Integer.parseInt(s);
            if (patientId == 0) return;
            if (!patients.containsKey(patientId))
                GUIHandler.show("You don't have any patient with id " + patientId);
        } while (!patients.containsKey(patientId));

        GUIHandler.show(patients.get(patientId).toString());

        Stage stage = new Stage();
        stage.setTitle("Patient " + patientId + " Options");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button b1 = new Button("See Latest Heart Rate");
        Button b2 = new Button("See Latest Blood Pressure");
        Button b3 = new Button("See Latest Oxygen Level");
        Button b4 = new Button("See Latest Temperature");
        Button b5 = new Button("See All Latest Vitals");
        Button b6 = new Button("View Complete Medical History");
        Button b7 = new Button("View Consultations By Doctor");
        Button b8 = new Button("View Consultations By Date");
        Button b9 = new Button("View Health Trend Graph");
        Button b10= new Button("Generate Patient Report");
        Button back= new Button("Go Back");

        int finalPatientId = patientId;
        b1.setOnAction(e -> {
            GUIHandler.show("Heart Rate: " + VitalsDatabase.getHeartRates(finalPatientId));
        });
        b2.setOnAction(e -> {
            GUIHandler.show("Blood Pressure: " + VitalsDatabase.getBloodPressures(finalPatientId));
        });
        b3.setOnAction(e -> {
            GUIHandler.show("Oxygen Level: " + VitalsDatabase.getOxygenLevels(finalPatientId));
        });
        b4.setOnAction(e -> {
            GUIHandler.show("Temperature" + VitalsDatabase.getTemperatures(finalPatientId));
        });
        b5.setOnAction(e -> {
            GUIHandler.show(VitalsDatabase.getAllVitals(finalPatientId));
        });
        b6.setOnAction(e -> {
            viewCompletePatientHistory(finalPatientId);
        });
        b7.setOnAction(e -> {
            viewPatientHistoryByDoctor(finalPatientId);
        });
        b8.setOnAction(e -> {
            viewPatientHistoryByDate(finalPatientId);
        });
        b9.setOnAction(e -> {
            VitalsDatabase.plotChart(
                    VitalsDatabase.generateHealthTrendGraph(finalPatientId)
            );
        });
        b10.setOnAction(e -> {
            ReportGenerator.generateReport(patients.get(finalPatientId));
        });
        back.setOnAction(e -> stage.close());

        root.getChildren().addAll(
                b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,
                back
        );

        stage.setScene(new Scene(root, 350, 550));
        stage.showAndWait();
    }


    private void viewCompletePatientHistory(int patientId) {
        GUIHandler.show(patients.get(patientId).getMedicalHistory().toString());
    }

    private void viewPatientHistoryByDoctor(int patientId) {
        String s = GUIHandler.prompt("Enter Doctor ID:");
        int doctorId = Integer.parseInt(s);

        Patient patient = patients.get(patientId);
        StringBuilder builder = new StringBuilder("Patient's history with the specified doctor:\n\n");
        for (int i = 0; i < patient.getMedicalHistory().getConsultations(doctorId).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(patient.getMedicalHistory().getConsultations(doctorId).get(i).toString())
                    .append("\n\n");

        GUIHandler.show(builder.toString());
    }

    private void viewPatientHistoryByDate(int patientId) {
        String input = GUIHandler.prompt("Enter Date (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Patient patient = patients.get(patientId);
        StringBuilder builder = new StringBuilder("Patient's history on specified date:\n\n");
        for (int i = 0; i < patient.getMedicalHistory().getConsultations(date).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(patient.getMedicalHistory().getConsultations(date).get(i).toString())
                    .append("\n\n");

        GUIHandler.show(builder.toString());
    }

    private void showAllChats() {
        ChatClient.showChatList(this);

        Stage stage = new Stage();
        stage.setTitle("Chat Menu");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button chatBtn = new Button("Chat with a Patient");
        Button backBtn = new Button("Go Back");

        chatBtn.setOnAction(e -> {
            chatWithPatient();
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(chatBtn, backBtn);

        stage.setScene(new Scene(root, 250, 150));
        stage.showAndWait();
    }


    private void chatWithPatient() {
        int patientId = getValidPatientId(Database.getPatients());
        if (patientId == 0) return;
        ChatClient.showChat(this, Database.getPatients().get(patientId));
    }

    private void videoCallPatient() {
        int patientId = getValidPatientId(Database.getPatients());
        if (patientId == 0) return;
        GUIHandler.show("Here's your video call link: " +
                VideoCall.generateLink(this, Database.getPatients().get(patientId))
        );
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

        Button cancelBtn = new Button("Cancel an Appointment");
        Button viewBtn   = new Button("View Patient Data");
        Button backBtn   = new Button("Go Back");

        cancelBtn.setOnAction(e -> {
            putAppointmentCancellation();
        });
        viewBtn.setOnAction(e -> {
            viewPatientData(patients);
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(cancelBtn, viewBtn, backBtn);

        stage.setScene(new Scene(root, 300, 200));
        stage.showAndWait();
    }


    private void putAppointmentCancellation() {
        int appointmentId = getValidAppointmentId(appointments);
        if (appointmentId == 0) return;
        AppointmentManager.finalizeCancellation(appointments.get(appointmentId));
        Administrator.addLog("Doctor Cancelled Appointment",
                this + " cancelled appointment " + appointments.get(appointmentId).toString());
    }

    public void removeAppointment(Appointment appointment) {
        removePatient(appointment.getPatient().getId());
        appointments.remove(appointment.getId());
        Administrator.addLog("Appointment and Patient removal from Doctor",
                "Appointment Manager removed " +
                        appointment.getPatient().getId() + " and " + appointment +
                        " from " + this + "'s patients and appointments"
        );
    }

    public void removePatient(int patientId) {
        int count = 0;
        for (Appointment appointment : appointments.values())
            if (appointment.getPatient().getId() == patientId)
                count++;
        if (count == 1)
            patients.remove(patientId);
    }

    private void listAppointmentRequests() {
        GUIHandler.show(
                "Your Appointment Requests:\n\n" +
                        Utilities.collectionToString(appointmentRequests.values(), "Request")
        );

        Stage stage = new Stage();
        stage.setTitle("Appointment Requests Menu");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button approveBtn = new Button("Approve an Appointment");
        Button rejectBtn  = new Button("Reject an Appointment");
        Button backBtn    = new Button("Go Back");

        approveBtn.setOnAction(e -> {
            approveAppointment();
        });
        rejectBtn.setOnAction(e -> {
            rejectAppointment();
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(approveBtn, rejectBtn, backBtn);

        stage.setScene(new Scene(root, 300, 200));
        stage.showAndWait();
    }


    private void approveAppointment() {
        int appointmentId = getValidAppointmentId(appointmentRequests);
        if (appointmentId == 0) return;

        Appointment currentAppointment = appointmentRequests.get(appointmentId);
        String input = GUIHandler.prompt("Enter Appointment Date (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        currentAppointment.setDate(date);

        addNewPatient(currentAppointment.getPatient());
        appointments.put(appointmentId, currentAppointment);
        AppointmentManager.finalizeAppointment(currentAppointment);
        appointmentRequests.remove(appointmentId);

        Administrator.addLog("Appointment Approval and Patient Addition to Doctor",
                this + " approved " + currentAppointment +
                        " and added " + currentAppointment.getPatient() + " to his patients"
        );
        GUIHandler.show("Appointment Approved and moved to your appointments!");
    }

    private void rejectAppointment() {
        int appointmentId = getValidAppointmentId(appointmentRequests);
        if (appointmentId == 0) return;
        AppointmentManager.finalizeRejection(appointmentRequests.get(appointmentId));
        Administrator.addLog("Appointment Rejection",
                this + " rejected " + appointmentRequests.get(appointmentId)
        );
        appointmentRequests.remove(appointmentId);
        GUIHandler.show("Appointment rejected!");
    }

    private void listAppointmentsToday() {
        HashMap<Integer, Appointment> appointmentsToday = new HashMap<>();
        HashMap<Integer, Patient> patientsToday = new HashMap<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDate().equals(LocalDate.now())) {
                appointmentsToday.put(appointment.getId(), appointment);
                patientsToday.put(appointment.getPatient().getId(), appointment.getPatient());
            }
        }

        GUIHandler.show(
                "Your Today's Appointments:\n\n" +
                        Utilities.collectionToString(appointmentsToday.values(), "Appointment")
        );

        Stage stage = new Stage();
        stage.setTitle("Today's Appointments");
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        Button viewBtn     = new Button("View Patient Data");
        Button feedbackBtn = new Button("Provide Feedback");
        Button backBtn     = new Button("Go Back");

        viewBtn.setOnAction(e -> {
            viewPatientData(patientsToday);
        });
        feedbackBtn.setOnAction(e -> {
            provideFeedback(appointmentsToday);
        });
        backBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(viewBtn, feedbackBtn, backBtn);

        stage.setScene(new Scene(root, 300, 200));
        stage.showAndWait();
    }


    private void provideFeedback(HashMap<Integer, Appointment> appointmentsToday) {
        int appointmentId = getValidAppointmentId(appointmentsToday);
        if (appointmentId == 0) return;
        Appointment currentAppointment = appointmentsToday.get(appointmentId);

        GUIHandler.show("This feedback will be visible to: " +
                currentAppointment.getPatient().getName()
        );

        String disease = GUIHandler.prompt("Enter diagnosed disease:");
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        int count = 0;
        String input;
        do {
            input = GUIHandler.prompt("Enter Medicine " + (++count) +
                    " (or 0 to go back with no feedback provided):"
            );
            if (input.equals("0")) return;
            String dosage = GUIHandler.prompt("Enter Dosage (or 0 to go back):");
            if (dosage.equals("0")) return;
            String schedule = GUIHandler.prompt("Enter Schedule (or 0 to go back):");
            if (schedule.equals("0")) return;
            String start = GUIHandler.prompt("Enter Starting Date (yyyy-MM-dd):");
            if (start.equals("0")) return;
            LocalDate startDate = LocalDate.parse(start,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            String end = GUIHandler.prompt("Enter Ending Date (yyyy-MM-dd):");
            if (end.equals("0")) return;
            LocalDate endDate = LocalDate.parse(end,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            prescriptions.add(new Prescription(startDate, input, dosage, schedule, endDate));
            input = GUIHandler.prompt(
                    "Press 'Y' to add another medicine, any other key to continue:"
            ).toUpperCase();
        } while (input.equals("Y"));

        String details = GUIHandler.prompt("Enter detailed feedback (or 0 to go back):");
        if (details.equals("0")) return;

        Feedback feedback = new Feedback(disease, prescriptions, details);
        currentAppointment.setFeedback(feedback);
        currentAppointment.getPatient()
                .getMedicalHistory()
                .addConsultation(this, LocalDate.now(), feedback);
        currentAppointment.setStatus("Complete");

        Administrator.addLog("Feedback Provision",
                this + " provided feedback on " + currentAppointment
        );
        Administrator.addLog("Consultation addition to Patient's Medical history",
                this + " added a new consultation to " +
                        currentAppointment.getPatient() + "'s medical history"
        );
        GUIHandler.show("Feedback submitted!");

        removeAppointment(currentAppointment);
        removePatient(currentAppointment.getPatient().getId());
    }

    public void addNewPatient(Patient newPatient) {
        patients.put(newPatient.getId(), newPatient);
    }

    public void addAppointmentRequest(Appointment request) {
        appointmentRequests.put(request.getId(), request);
        Administrator.addLog("Appointment Request addition to Doctor",
                "Appointment Manager added " + request +
                        " to " + this + "'s appointment requests"
        );
    }

    public void addNewSMSNotification(String newNotification) {
        notifications.add(0, newNotification);
    }

    private int getValidAppointmentId(HashMap<Integer, Appointment> lookupFrom) {
        int appointmentId;
        do {
            appointmentId = Integer.parseInt(
                    GUIHandler.prompt("Enter Appointment ID (or 0 to go back):")
            );
            if (appointmentId == 0) return 0;
            if (!lookupFrom.containsKey(appointmentId))
                GUIHandler.show("No appointment with ID: " + appointmentId);
        } while (!lookupFrom.containsKey(appointmentId));
        return appointmentId;
    }

    private int getValidPatientId(HashMap<Integer, Patient> lookupFrom) {
        int patientId;
        do {
            patientId = Integer.parseInt(
                    GUIHandler.prompt("Enter the ID of the patient (or 0 to go back):")
            );
            if (patientId == 0) return 0;
            if (!lookupFrom.containsKey(patientId))
                GUIHandler.show("There are no patients with ID: " + patientId);
        } while (!lookupFrom.containsKey(patientId));
        return patientId;
    }

    public HashMap<Integer, Appointment> getAppointmentRequests() {
        return appointmentRequests;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + getId() + "\t\t" + "Name: " + getName();
    }
}
