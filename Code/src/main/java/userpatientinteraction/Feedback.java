package userpatientinteraction;

import java.util.ArrayList;

public class Feedback {
    private String disease; // The disease diagnosed during the consultation
    private ArrayList<Prescription> prescriptions; // List of prescriptions given to the patient
    private String feedback; // Additional feedback given by the doctor

    // Constructor to initialize the Feedback object with disease, prescriptions, and feedback
    public Feedback(String disease, ArrayList<Prescription> prescriptions, String feedback) {
        this.disease = disease; // Assigning the disease to the feedback
        this.prescriptions = prescriptions; // Assigning the list of prescriptions to the feedback
        this.feedback = feedback; // Assigning the additional feedback to the feedback
    }

    // Method to get the diagnosed disease
    public String getDisease() {
        return disease; // Returning the disease diagnosed
    }

    // Method to get the list of prescriptions
    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions; // Returning the list of prescriptions
    }

    // Method to get the doctor's feedback
    public String getFeedback() {
        return feedback; // Returning the feedback provided by the doctor
    }

    @Override
    public String toString() {
        StringBuilder prescriptionBuilder = new StringBuilder(); // StringBuilder to build the prescription list
        prescriptionBuilder.append("Prescriptions:\n"); // Header for prescriptions
        for (int i = 0; i < getPrescriptions().size(); i++) // Looping through prescriptions
            prescriptionBuilder.append("Prescription ")
                    .append(i + 1) // Indexing the prescriptions
                    .append(": ")
                    .append(getPrescriptions().get(i).toString()) // Adding the string representation of each prescription
                    .append("\n");

        // Returning the formatted feedback string, including disease, prescriptions, and feedback details
        return "Diagnosed Disease: " + getDisease() + "\n\n" +
                prescriptionBuilder + "\n" +
                "Feedback Details: " + getFeedback();
    }
}
