package usermanagement;

import appointmentscheduling.*;
import chatandvideoconsultation.ChatClient;
import chatandvideoconsultation.VideoCall;
import healthdatahandling.VitalsDatabase;
import notificationsreminders.SMSNotification;
import userpatientinteraction.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Doctor extends User {
    private HashMap<Integer, Appointment> appointmentRequests = new HashMap<>();
    private HashMap<Integer, Appointment> appointments = new HashMap<>();
    private HashMap<Integer, Patient> patients = new HashMap<>();
    private ArrayList<String> notifications = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public Doctor(String name, String email, String phone) {
        super(name, email, phone);
        Administrator.addDoctor(this); // Adding this doctor to admin's record
        Administrator.addLog("Doctor Registration", this + " registered in system doctors"); // Logging doctor registration
    }

    public void getOptions() {
        while (true) {
            System.out.println("Enter 1 to list all patients");
            System.out.println("Enter 2 to list all appointments");
            System.out.println("Enter 3 to list appointment requests");
            System.out.println("Enter 4 to list appointments due today");
            System.out.println("Enter 5 to view your SMS Notifications");
            System.out.println("Enter 6 to show all of your chats");
            System.out.println("Enter 7 to chat with a patient");
            System.out.println("Enter 8 to video call a patient");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    listPatients();
                    break;
                case 2:
                    // Listing appointments only; actual generation and deletion are managed by AppointmentManager
                    listAppointments();
                    break;
                case 3:
                    listAppointmentRequests();
                    break;
                case 4:
                    listAppointmentsToday();
                    break;
                case 5:
                    System.out.println("Your SMS Notifications: ");
                    System.out.println(Utilities.collectionToString(notifications, "SMS Notification"));
                    break;
                case 6:
                    showAllChats();
                    break;
                case 7:
                    chatWithPatient();
                    break;
                case 8:
                    videoCallPatient();
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void listPatients() {
        System.out.println("Your Patients:\n");
        System.out.println(Utilities.collectionToString(patients.values(), "Patient")); // Printing all patients

        while (true) {
            System.out.println("Enter 1 to view patient data");
            System.out.println("Enter 2 to chat with a patient");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    viewPatientData(patients); // Viewing vitals and history
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void viewPatientData(HashMap<Integer, Patient> patients) {
        int patientId;
        do {
            System.out.print("Enter Patient ID (or 0 to go back): ");
            patientId = Utilities.readInt();
            if (patientId == 0) return;
            if (!patients.containsKey(patientId))
                System.out.println("You don't have any patient with id " + patientId);
        } while (!patients.containsKey(patientId));

        System.out.println(patients.get(patientId)); // Displaying patient's summary

        while (true) {
            System.out.println();
            System.out.println("Enter 1 to see Heart Rate");
            System.out.println("Enter 2 to see Blood Pressure");
            System.out.println("Enter 3 to see Oxygen Level");
            System.out.println("Enter 4 to see Temperature");
            System.out.println("Enter 5 to see All Vitals");
            System.out.println("Enter 6 to view complete medical history");
            System.out.println("Enter 7 to view consultations from a particular doctor");
            System.out.println("Enter 8 to view consultations on a particular date");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    System.out.println("Heart Rate: " + VitalsDatabase.getHeartRate(patientId)); // Fetching heart rate
                    break;
                case 2:
                    System.out.println("Blood Pressure: " + VitalsDatabase.getBloodPressure(patientId)); // Fetching BP
                    break;
                case 3:
                    System.out.println("Oxygen Level: " + VitalsDatabase.getOxygenLevel(patientId)); // Fetching oxygen
                    break;
                case 4:
                    System.out.println("Temperature" + VitalsDatabase.getTemperature(patientId)); // Fetching temp
                    break;
                case 5:
                    System.out.println(VitalsDatabase.getAllVitals(patientId)); // Displaying all vitals together
                    break;
                case 6:
                    viewCompletePatientHistory(patientId); // Displaying full medical record
                    break;
                case 7:
                    viewPatientHistoryByDoctor(patientId); // Displaying consultations with a specific doctor
                    break;
                case 8:
                    viewPatientHistoryByDate(patientId); // Displaying consultations on a specific date
                    break;
                default:
                    System.out.println("Please Enter a valid value");
            }
        }
    }

    private void viewCompletePatientHistory(int patientId) {
        System.out.println(patients.get(patientId).getMedicalHistory()); // Printing the whole history object
    }

    private void viewPatientHistoryByDoctor(int patientId) {
        System.out.print("Enter Doctor ID: ");
        int doctorId = Utilities.readInt();

        Patient patient = patients.get(patientId);
        StringBuilder builder = new StringBuilder();
        builder.append("Patient's history with the specified doctor:\n\n");

        for (int i = 0; i < patient.getMedicalHistory().getConsultations(doctorId).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(patient.getMedicalHistory().getConsultations(doctorId).get(i).toString())
                    .append("\n\n");

        System.out.println(builder); // Displaying history with that doctor
    }

    private void viewPatientHistoryByDate(int patientId) {
        scanner.nextLine(); // Clearing buffer before reading string
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String input = scanner.nextLine().trim();
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Patient patient = patients.get(patientId);
        StringBuilder builder = new StringBuilder();
        builder.append("Patient's history on specified date:\n\n");

        for (int i = 0; i < patient.getMedicalHistory().getConsultations(date).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(patient.getMedicalHistory().getConsultations(date).get(i).toString())
                    .append("\n\n");

        System.out.println(builder); // Displaying history on that date
    }

    private void showAllChats() {
        ChatClient.showChatList(this);
        while (true) {
            System.out.println("Enter 1 to chat with a patient");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();
            switch (input) {
                case 0:
                    return;
                case 1:
                    chatWithPatient();
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void chatWithPatient() {
        int patientId = getValidPatientId(Database.getPatients());
        if (patientId == 0) return;
        ChatClient.showChat(this, Database.getPatients().get(patientId));
    }

    private void videoCallPatient() {
        int patientId = getValidPatientId(Database.getPatients());
        if (patientId == 0) return;
        System.out.println("Here's your video call link: " +
                VideoCall.generateLink(this, Database.getPatients().get(patientId)));
    }

    private void listAppointments() {
        while (true) {
            System.out.println("Your Appointments:\n");
            System.out.println(Utilities.collectionToString(appointments.values(), "Appointment")); // Displaying appointment list

            System.out.println("Enter 1 to cancel an appointment");
            System.out.println("Enter 2 to view patient data");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    putAppointmentCancellation(); // Doctor requests cancellation
                    break;
                case 2:
                    viewPatientData(patients); // Allow viewing of patient info from appointment menu
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void putAppointmentCancellation() {
        // Getting a valid appointment ID from the doctor's appointments map
        int appointmentId = getValidAppointmentId(appointments);
        if (appointmentId == 0) return;

        // Finalizing cancellation through AppointmentManager
        AppointmentManager.finalizeCancellation(appointments.get(appointmentId));

        // Logging the cancellation
        Administrator.addLog("Doctor Cancelled Appointment",
                this + " cancelled appointment " + appointments.get(appointmentId).toString());
    }

    public void removeAppointment(Appointment appointment) {
        // Removing the patient only if it's their last appointment
        removePatient(appointment.getPatient().getId());

        // Removing the appointment from doctor's list
        appointments.remove(appointment.getId());

        // Logging both removals
        Administrator.addLog("Appointment and Patient removal from Doctor",
                "Appointment Manager removed " + appointment.getPatient().getId() + " and " + appointment +
                        " from " + this + "'s patients and appointments");
    }

    public void removePatient(int patientId) {
        int count = 0;

        // Counting how many appointments this patient has with the doctor
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatient().getId() == patientId)
                count++;
        }

        // If only one appointment existed, remove the patient
        if (count == 1)
            patients.remove(patientId);
    }

    private void listAppointmentRequests() {
        while (true) {
            System.out.println("Your Appointment Requests:\n");
            System.out.println(Utilities.collectionToString(appointmentRequests.values(), "Request"));

            // User options for request handling
            System.out.println("Enter 1 to approve an appointment");
            System.out.println("Enter 2 to reject an appointment");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    approveAppointment();
                    break;
                case 2:
                    rejectAppointment();
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void approveAppointment() {
        // Validating appointment request ID
        int appointmentId = getValidAppointmentId(appointmentRequests);
        if (appointmentId == 0) return;

        Appointment currentAppointment = appointmentRequests.get(appointmentId);

        // Setting date for approved appointment
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        // scanner.nextLine(); // Clearing buffer
        String input = scanner.nextLine().trim();
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        currentAppointment.setDate(date);

        // Adding patient if not already in list
        addNewPatient(currentAppointment.getPatient());

        // Moving request to appointments list
        appointments.put(appointmentId, currentAppointment);
        AppointmentManager.finalizeAppointment(currentAppointment);

        // Removing request after approval
        appointmentRequests.remove(appointmentId);

        // Logging the approval
        Administrator.addLog("Appointment Approval and Patient Addition to Doctor", this + " approved " + currentAppointment +
                " and added " + currentAppointment.getPatient() + " to his patients");
        System.out.println("Appointment Approved and moved to your appointments!");
    }

    private void rejectAppointment() {
        // Validating appointment request ID
        int appointmentId = getValidAppointmentId(appointmentRequests);
        if (appointmentId == 0) return;

        // Finalizing rejection
        AppointmentManager.finalizeRejection(appointmentRequests.get(appointmentId));

        // Logging before removal to avoid null in log message
        Administrator.addLog("Appointment Rejection", this + " rejected " + appointmentRequests.get(appointmentId));

        // Removing from request list
        appointmentRequests.remove(appointmentId);

        System.out.println("Appointment rejected!");
    }

    private void listAppointmentsToday() {
        while (true) {
            // Filtering today's appointments and relevant patients
            HashMap<Integer, Appointment> appointmentsToday = new HashMap<>();
            HashMap<Integer, Patient> patientsToday = new HashMap<>();
            for (Appointment appointment : appointments.values())
                if (appointment.getDate().equals(LocalDate.now())) {
                    appointmentsToday.put(appointment.getId(), appointment);
                    patientsToday.put(appointment.getPatient().getId(), appointment.getPatient());
                }

            System.out.println("Your Today's Appointments:\n");
            System.out.println(Utilities.collectionToString(appointmentsToday.values(), "Appointment"));

            // Actions related to today's appointments
            System.out.println("\nEnter 1 to view patient data");
            System.out.println("Enter 2 to provide feedback to a patient on an appointment");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    viewPatientData(patientsToday);
                    break;
                case 2:
                    provideFeedback(appointmentsToday);
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void provideFeedback(HashMap<Integer, Appointment> appointmentsToday) {
        // Validating selected appointment
        int appointmentId = getValidAppointmentId(appointmentsToday);
        if (appointmentId == 0) return;

        Appointment currentAppointment = appointmentsToday.get(appointmentId);

        // Prompting doctor for feedback details
        System.out.println("This feedback will be visible to: " + currentAppointment.getPatient().getName());
        System.out.println();

        System.out.print("Enter diagnosed disease: ");
        scanner.nextLine(); // Clearing buffer
        String disease = scanner.nextLine().trim();

        // Collecting prescription details
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        int count = 0;
        String input;
        do {
            System.out.print("Enter Medicine " + (++count) + " (or 0 to go back with no feedback provided): ");
            String medicine = scanner.nextLine().trim();
            if (medicine.equals("0")) return;

            System.out.print("Enter Dosage (or 0 to go back with no feedback provided): ");
            String dosage = scanner.nextLine().trim();
            if (dosage.equals("0")) return;

            System.out.print("Enter Schedule (or 0 to go back with no feedback provided): ");
            String schedule = scanner.nextLine();
            if (schedule.equals("0")) return;

            System.out.println("Enter Starting Date of this medicine (or 0 to go back with no feedback provided): ");
            input = scanner.nextLine().trim();
            if (input.equals("0")) return;
            LocalDate startDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.println("Enter Ending Date of this medicine (or 0 to go back with no feedback provided): ");
            input = scanner.nextLine().trim();
            if (input.equals("0")) return;
            LocalDate endDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            prescriptions.add(new Prescription(startDate, medicine, dosage, schedule, endDate));

            System.out.print("Press 'Y' to add another medicine. Press any other key to continue with feedback: ");
            input = scanner.next().toUpperCase();
            scanner.nextLine(); // Clearing buffer
        } while (input.equals("Y"));

        // Getting detailed feedback from the doctor
        System.out.print("Enter detailed feedback (or 0 to go back with no feedback provided): ");
        String details = scanner.nextLine();
        if (details.trim().equals("0")) return;

        Feedback feedback = new Feedback(disease, prescriptions, details);
        currentAppointment.setFeedback(feedback);

        // Adding feedback as a consultation to patient's medical history
        currentAppointment.getPatient().getMedicalHistory().addConsultation(
                this, LocalDate.now(), feedback
        );

        // Marking appointment as completed
        currentAppointment.setStatus("Complete");

        // Logging updates
        Administrator.addLog("Feedback Provision", this + " provided feedback on " + currentAppointment);
        Administrator.addLog("Consultation addition to Patient's Medical history",
                this + " added a new consultation to " + currentAppointment.getPatient() + "'s medical history");

        System.out.println("Feedback submitted!");

        // Removing appointment and patient (if applicable)
        removeAppointment(currentAppointment);
        removePatient(currentAppointment.getPatient().getId());
    }

    public void addNewPatient(Patient newPatient) {
        // Simply adding new patient to doctor's map
        patients.put(newPatient.getId(), newPatient);
    }

    public void addAppointmentRequest(Appointment request) {
        // Storing appointment request
        appointmentRequests.put(request.getId(), request);

        // Logging the action
        Administrator.addLog("Appointment Request addition to Doctor",
                "Appointment Manager added " + request + " to " +
                        this + "'s appointment requests");
    }

    public void addNewSMSNotification(String newNotification) {
        notifications.add(0, newNotification);
    }

    private int getValidAppointmentId(HashMap<Integer, Appointment> lookupFrom) {
        int appointmentId;
        do {
            System.out.print("Enter Appointment ID (or 0 to go back): ");
            appointmentId = Utilities.readInt();
            if (appointmentId == 0) return 0;

            // Ensuring the entered ID exists in the provided map
            if (!lookupFrom.containsKey(appointmentId))
                System.out.println("No appointment with ID: " + appointmentId);
        } while (!lookupFrom.containsKey(appointmentId));

        return appointmentId;
    }

    private int getValidPatientId(HashMap<Integer, Patient> lookupFrom) {
        int patientId;
        // Asking user to enter valid patient ID
        do {
            System.out.print("Enter the ID of the patient (or 0 to go back): ");
            patientId = Utilities.readInt();
            if (patientId == 0) return 0;
            if (!lookupFrom.containsKey(patientId))
                System.out.println("There are no patients with ID: " + patientId);
        } while (!lookupFrom.containsKey(patientId));

        return patientId;
    }


    @Override
    public String toString() {
        // Concise doctor info display
        return "Doctor ID: " + getId() + "\t\t" +
                "Name: " + getName();
    }
}

