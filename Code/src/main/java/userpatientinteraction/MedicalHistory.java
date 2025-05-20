package userpatientinteraction;

import usermanagement.Doctor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MedicalHistory implements Serializable {
    private ArrayList<Consultation> consultations = new ArrayList<>(); // List to store all consultations

    // Method to add a consultation to the medical history
    public void addConsultation(Doctor doctor, LocalDate date, Feedback feedback) {
        consultations.add(new Consultation(doctor, date, feedback)); // Creating a new Consultation and adding it to the list
    }

    // Method to get all consultations
    public ArrayList<Consultation> getConsultations() {
        return consultations; // Returning the entire list of consultations
    }

    // Method to get consultations for a specific doctor by doctor ID
    public ArrayList<Consultation> getConsultations(int doctorId) {
        ArrayList<Consultation> doctorConsultations = new ArrayList<>(); // List to store consultations for the specified doctor
        for (Consultation consultation : consultations) {
            if (consultation.getDoctor().getId() == doctorId) // Checking if the doctor ID matches
                doctorConsultations.add(consultation); // Adding consultation to the list if it matches
        }
        return doctorConsultations; // Returning the list of consultations for the specific doctor
    }

    // Method to get consultations for a specific date
    public ArrayList<Consultation> getConsultations(LocalDate date) {
        ArrayList<Consultation> dateConsultations = new ArrayList<>(); // List to store consultations for the specified date
        for (Consultation consultation : consultations) {
            if (consultation.getDate().equals(date)) // Checking if the date matches
                dateConsultations.add(consultation); // Adding consultation to the list if the date matches
        }
        return dateConsultations; // Returning the list of consultations for the specific date
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // StringBuilder to construct the medical history string
        builder.append("Medical History: \n\n"); // Adding header to the string
        for (int i = 0; i < getConsultations().size(); i++) // Looping through all consultations
            builder.append("Consultation ")
                    .append(i + 1) // Appending the consultation number
                    .append(": \n")
                    .append(getConsultations().get(i).toString()) // Appending the string representation of each consultation
                    .append("\n\n"); // Adding line break between consultations
        return builder.toString(); // Returning the final medical history string
    }
}
