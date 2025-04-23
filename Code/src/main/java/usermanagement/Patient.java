package usermanagement;

import appointmentscheduling.*;
import chatandvideoconsultation.ChatClient;
import chatandvideoconsultation.VideoCall;
import emergencyalertsystem.EmergencyAlert;
import emergencyalertsystem.PanicButton;
import healthdatahandling.VitalsDatabase;
import userpatientinteraction.MedicalHistory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Patient extends User {
    private HashMap<Integer, Appointment> appointments = new HashMap<>();
    private HashMap<Integer, Doctor> doctors = new HashMap<>();
    private MedicalHistory medicalHistory = new MedicalHistory();
    private PanicButton panicButton = new PanicButton();
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<String> notifications = new ArrayList<>();

    public Patient(String name, String email, String phone) {
        super(name, email, phone);
        Administrator.addPatient(this); // Adding patient to system
        Administrator.addLog("Patient Registration", this + " registered in system patients"); // Logging registration
    }

    public void getOptions() {
        while (true) {
            // Displaying user options in loop until user exits
            System.out.println("Enter 1 to list all appointments");
            System.out.println("Enter 2 to list all doctors");
            System.out.println("Enter 3 to request an appointment");
            System.out.println("Enter 4 to view your medical history");
            System.out.println("Enter 5 to view your medical history with a particular doctor");
            System.out.println("Enter 6 to view your medical history for a particular date");
            System.out.println("Enter 7 to press your panic button and send an emergency response request to all your current doctors");
            System.out.println("Enter 8 to view your SMS Notifications");
            System.out.println("Enter 9 to show all of your chats");
            System.out.println("Enter 10 to chat with a doctor");
            System.out.println("Enter 11 to video call a doctor");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    listAppointments(); // Showing appointments
                    break;
                case 2:
                    listAllDoctors(); // Showing doctors
                    break;
                case 3:
                    requestAppointment(); // Requesting new appointment
                    break;
                case 4:
                    viewCompleteHistory(); // Viewing all medical history
                    break;
                case 5:
                    viewHistoryByDoctor(); // Viewing history with specific doctor
                    break;
                case 6:
                    viewHistoryByDate(); // Viewing history for specific date
                    break;
                case 7:
                    getPanicButton().press(this);  // Pressing current patient's panic button
                    break;
                case 8:
                    System.out.println("Your SMS Notifications: ");
                    System.out.println(Utilities.collectionToString(notifications, "SMS Notification"));
                case 9:
                    showAllChats();
                    break;
                case 10:
                    chatWithDoctor(); // Starting a chat
                    break;
                case 11:
                    videoCallDoctor();
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void listAppointments() {
        while (true) {
            // Listing appointments and showing further options
            System.out.println("Your Appointments:\n");
            System.out.println(Utilities.collectionToString(appointments.values(), "Appointment"));
            System.out.println("Enter 1 to request cancellation of an appointment");
            System.out.println("Enter 2 to view doctor's feedback on an appointment");
            System.out.println("Enter 3 to remove cancelled appointments");
            System.out.println("Enter 4 to remove completed appointments");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    requestAppointmentCancellation(); // Requesting cancellation
                    break;
                case 2:
                    viewFeedback(); // Viewing feedback from doctor
                    break;
                case 3:
                    // Removing all cancelled appointments
                    for (Appointment appointment : appointments.values())
                        if (appointment.getStatus().equals("Cancelled"))
                            removeAppointment(appointment);
                    System.out.println("Cancelled appointments removed!");
                    break;
                case 4:
                    // Removing all completed appointments
                    for (Appointment appointment : appointments.values())
                        if (appointment.getStatus().equals("Complete"))
                            removeAppointment(appointment);
                    System.out.println("Completed appointments removed!");
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void viewFeedback() {
        int appointmentId = getValidAppointmentId(); // Asking for valid appointment ID
        if (appointmentId == 0) return;

        // Showing feedback based on appointment status
        if (appointments.get(appointmentId).getStatus().equals("Complete"))
            System.out.println(appointments.get(appointmentId).getFeedback());
        else if (appointments.get(appointmentId).getStatus().equals("Pending"))
            System.out.println("The appointment is still pending. No feedback yet!");
        else
            System.out.println("This appointment has been cancelled! Check appointment status");
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
            System.out.println("This appointment is already not pending. Check appointment status");
    }

    private void removeAppointment(Appointment appointment) {
        removeDoctor(appointment.getDoctor().getId()); // Removing doctor if no other appointment remains
        appointments.remove(appointment.getId()); // Removing appointment from record
        System.out.println("Your appointment " + appointment + " has been removed");
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
        while (true) {
            // Displaying all doctors and option to request appointment
            System.out.println("Available Doctors:\n");
            System.out.println(Utilities.collectionToString(Database.getDoctors().values(), "Doctor"));
            System.out.println("Enter 1 to request an appointment");
            System.out.println("Enter 2 to chat with a doctor");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return;
                case 1:
                    requestAppointment(); // Requesting appointment
                    break;
                case 2:
                    chatWithDoctor(); // Starting a chat
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void requestAppointment() {
        int doctorId;
        // Asking user to enter valid doctor ID
        do {
            System.out.print("Enter the ID of the doctor (or 0 to go back): ");
            doctorId = Utilities.readInt();
            if (doctorId == 0) return;
            if (!Database.getDoctors().containsKey(doctorId))
                System.out.println("There are no doctors with ID: " + doctorId);
        } while (!Database.getDoctors().containsKey(doctorId));

        AppointmentManager.requestFromDoctor(doctorId, this); // Creating appointment request
        submitVitals(doctorId); // Asking user to submit vitals
        Administrator.addLog("Appointment Request and Vitals Submission",
                this + " requested an appointment from doctor ID: " + doctorId +
                        " and submitted his vitals for this appointment");
        System.out.println("Appointment requested! If approved, it will automatically be added to your appointments");
    }

    private void submitVitals(int doctorId) {
        System.out.println("Please submit your vitals for further actions");
        float heartRate = getValidatedVital("Heart Rate (BPM)" , 30f,  200f);
        if (heartRate < 60f || heartRate > 100f) {
            EmergencyAlert alert = new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Heart Rate out of threshold!");
            alert.trigger();
        }

        float bloodPressure = getValidatedVital("Blood Pressure (Systolic mmHg)", 70f, 250f);
        if (bloodPressure < 90f || bloodPressure > 140f) {
            EmergencyAlert alert = new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Blood Pressure out of threshold!");
            alert.trigger();
        }

        float oxygenLevel = getValidatedVital("Oxygen Level (%)", 50f, 100f);
        if (oxygenLevel < 95f || oxygenLevel > 100f) {
            EmergencyAlert alert = new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Oxygen Level out of threshold!");
            alert.trigger();
        }

        float temperature = getValidatedVital("Temperature (Fahrenheit)", 90f, 110f);
        if (temperature < 97f || temperature > 99.5f) {
            EmergencyAlert alert = new EmergencyAlert(this, Database.getDoctors().get(doctorId), "Temperature out of threshold!");
            alert.trigger();
        }

        VitalsDatabase.addPatientVitals(this.getId(), heartRate, bloodPressure, oxygenLevel, temperature); // Saving vitals
    }

    private float getValidatedVital(String prompt, float minVal, float maxVal) {
        float value;
        do {
            System.out.print("Enter your " + prompt + " { " + minVal + "-" + maxVal + " } " + ": ");
            value = Utilities.readFloat();
            if (value < minVal || value > maxVal)
                System.out.println("Invalid Value. " + prompt + " can have a value between " + minVal + " and "
                        + maxVal + " only. Please enter again!");
        } while (value < minVal || value > maxVal);
        return value;
    }

    private void viewCompleteHistory() {
        System.out.println(medicalHistory); // Printing all medical history
    }

    private void viewHistoryByDoctor() {
        System.out.print("Enter Doctor ID: ");
        int doctorId = Utilities.readInt();
        StringBuilder builder = new StringBuilder();
        builder.append("Your history with the specified doctor:\n\n");
        // Appending consultation records with selected doctor
        for (int i = 0; i < getMedicalHistory().getConsultations(doctorId).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(getMedicalHistory().getConsultations(doctorId).get(i).toString())
                    .append("\n\n");
        System.out.println(builder);
    }

    private void viewHistoryByDate() {
        scanner.nextLine(); // Clearing buffer before taking date input
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String input = scanner.nextLine().trim();
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        StringBuilder builder = new StringBuilder();
        builder.append("Patient's history on specified date:\n\n");
        // Appending consultation records from the specified date
        for (int i = 0; i < getMedicalHistory().getConsultations(date).size(); i++)
            builder.append("Consultation ")
                    .append(i + 1)
                    .append(": \n")
                    .append(getMedicalHistory().getConsultations(date).get(i).toString())
                    .append("\n\n");
        System.out.println(builder);
    }

    private void showAllChats() {
        ChatClient.showChatList(this);
        while (true) {
            System.out.println("Enter 1 to chat with a doctor");
            System.out.println("Enter 0 to go back");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();
            switch (input) {
                case 0:
                    return;
                case 1:
                    chatWithDoctor();
                    break;
                default:
                    System.out.println("Please enter a valid value");
            }
        }
    }

    private void chatWithDoctor () {
        int doctorId = getValidDoctorId(Database.getDoctors());
        if (doctorId == 0) return;
        ChatClient.showChat(this, Database.getDoctors().get(doctorId));
    }

    private void videoCallDoctor() {
        int doctorId = getValidDoctorId(Database.getDoctors());
        if (doctorId == 0) return;
        System.out.println("Here's your video call link: " +
                VideoCall.generateLink(this, Database.getDoctors().get(doctorId)));
    }

    public void addNewAppointment(Appointment newAppointment) {
        appointments.put(newAppointment.getId(), newAppointment); // Adding new appointment
        addNewDoctor(newAppointment.getDoctor()); // Adding associated doctor
        Administrator.addLog("Appointment and Doctor addition to Patient",
                "Appointment Manager added " + newAppointment + " and " + newAppointment.getDoctor() + " to " +
                        this + "'s appointments and doctors");
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
        // Asking user to enter valid doctor ID
        do {
            System.out.print("Enter the ID of the doctor (or 0 to go back): ");
            doctorId = Utilities.readInt();
            if (doctorId == 0) return 0;
            if (!lookupFrom.containsKey(doctorId))
                System.out.println("There are no doctors with ID: " + doctorId);
        } while (!lookupFrom.containsKey(doctorId));

        return doctorId;
    }

    private int getValidAppointmentId() {
        int appointmentId;
        // Asking user repeatedly until a valid appointment ID is provided
        do {
            System.out.print("Enter Appointment ID (or 0 to go back): ");
            appointmentId = Utilities.readInt();
            if (appointmentId == 0) return 0;
            if (!appointments.containsKey(appointmentId))
                System.out.println("No appointment with ID: " + appointmentId);
        } while (!appointments.containsKey(appointmentId));
        return appointmentId;
    }

    @Override
    public String toString() {
        return "Patient ID: " + getId() + "\t\t" +
                "Patient Name: " + getName(); // Converting patient object to string
    }
}
