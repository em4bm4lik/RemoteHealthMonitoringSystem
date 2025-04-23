package userpatientinteraction;

import java.time.LocalDate;

public class Prescription {
    private LocalDate startDate;
    private String medicine; // The name of the medicine prescribed
    private String dosage; // The dosage of the medicine prescribed
    private String schedule; // The schedule for taking the medicine
    private LocalDate endDate;

    // Constructor to initialize the Prescription object with medicine, dosage, and schedule
    public Prescription(LocalDate startDate, String medicine, String dosage, String schedule, LocalDate endDate) {
        this.startDate = startDate;
        this.medicine = medicine; // Assigning the medicine name to the prescription
        this.dosage = dosage; // Assigning the dosage of the medicine
        this.schedule = schedule; // Assigning the schedule for taking the medicine
        this.endDate = endDate;
    }

    // Method to get the name of the medicine prescribed
    public String getMedicine() {
        return medicine; // Returning the medicine name
    }

    // Method to get the dosage of the medicine
    public String getDosage() {
        return dosage; // Returning the dosage of the medicine
    }

    // Method to get the schedule for taking the medicine
    public String getSchedule() {
        return schedule; // Returning the schedule for taking the medicine
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Start Date: " + getStartDate() + "\t" +
                "Medicine: " + getMedicine() + "\t" + // Medicine information
                "Dosage: " + getDosage() + "\t" + // Dosage information
                "Schedule: " + getSchedule() + "\t" + // Schedule information
                "End Date: " + getEndDate();
    }
}
