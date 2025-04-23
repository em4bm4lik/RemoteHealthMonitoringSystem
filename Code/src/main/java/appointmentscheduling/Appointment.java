package appointmentscheduling;

import usermanagement.Doctor;
import usermanagement.Patient;
import userpatientinteraction.Feedback;

import java.time.LocalDate;
import java.util.ArrayList;

public class Appointment {
    private static int temp; // Static variable to keep track of the appointment ID
    private int id; // Unique ID for each appointment
    private Doctor doctor; // The doctor associated with this appointment
    private Patient patient; // The patient associated with this appointment
    private LocalDate date; // The date of the appointment
    private String status; // The current status of the appointment
    private Feedback feedback; // Feedback given after the appointment

    public Appointment(Doctor doctor, Patient patient) {
        this.id = ++temp; // Incrementing and assigning a new ID to the appointment
        this.doctor = doctor; // Setting the doctor for the appointment
        this.patient = patient; // Setting the patient for the appointment
        this.date = null; // Initially, no date is assigned to the appointment
        this.status = "Requested"; // The initial status is set to "Requested"
        this.feedback = new Feedback("", new ArrayList<>(), ""); // Initializing the feedback with empty values
    }

    public int getId() {
        return id; // Returning the appointment's ID
    }

    public Doctor getDoctor() {
        return doctor; // Returning the doctor of the appointment
    }

    public Patient getPatient() {
        return patient; // Returning the patient of the appointment
    }

    public LocalDate getDate() {
        return date; // Returning the date of the appointment
    }

    public String getStatus() {
        return status; // Returning the status of the appointment
    }

    public void setDate(LocalDate date) {
        this.date = date; // Setting the date of the appointment
    }

    public void setStatus(String status) {
        this.status = status; // Setting the status of the appointment
    }

    public Feedback getFeedback() {
        return feedback; // Returning the feedback for the appointment
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback; // Setting the feedback for the appointment
    }

    @Override
    public String toString() {
        return "Appointment ID: " + getId() + "\n" +
                "Doctor: " + getDoctor().toString() + "\n" +
                "Patient: " + getPatient().toString() + "\n" +
                "Date: " + ((getStatus().equals("Pending") || getStatus().equals("Complete") || getStatus().equals("Cancelled")) ?
                getDate().toString() : "No date assigned") + "\n" + // Showing the date only if the status is pending, complete, or cancelled
                "Status: " + getStatus() + "\n" +
                "Feedback:\n" + getFeedback().toString(); // Showing the feedback
    }
}
