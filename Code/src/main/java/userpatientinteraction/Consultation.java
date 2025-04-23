package userpatientinteraction;

import usermanagement.Doctor;

import java.time.LocalDate;

public class Consultation {
    private Doctor doctor; // The doctor associated with the consultation
    private LocalDate date; // The date of the consultation
    private Feedback feedback; // The feedback provided after the consultation

    // Constructor to initialize the Consultation object with doctor, date, and feedback
    public Consultation(Doctor doctor, LocalDate date, Feedback feedback) {
        this.doctor = doctor; // Assigning doctor to the consultation
        this.date = date; // Assigning date to the consultation
        this.feedback = feedback; // Assigning feedback to the consultation
    }

    // Method to get the doctor associated with the consultation
    public Doctor getDoctor() {
        return doctor; // Returning the doctor of the consultation
    }

    // Method to get the date of the consultation
    public LocalDate getDate() {
        return date; // Returning the date of the consultation
    }

    // Method to get the feedback of the consultation
    public Feedback getFeedback() {
        return feedback; // Returning the feedback of the consultation
    }

    @Override
    public String toString() {
        return "Doctor: " + getDoctor().toString() + "\n" + // Doctor's information as a string
                "Date: " + getDate() + "\n" + // Date of the consultation as a string
                "Feedback: " + getFeedback().toString(); // Feedback information as a string
    }
}
