package healthdatahandling;

import java.io.Serializable;

public class VitalSign implements Serializable {
    private float heartRate; // Heart rate of the patient
    private float bloodPressure; // Blood pressure of the patient
    private float oxygenLevel; // Oxygen level in the blood
    private float temperature; // Body temperature of the patient

    // Constructor to initialize the vital signs (heart rate, blood pressure, oxygen level, and temperature)
    public VitalSign(float heartRate, float bloodPressure, float oxygenLevel, float temperature) {
        this.heartRate = heartRate; // Assigning heart rate to the vital sign
        this.bloodPressure = bloodPressure; // Assigning blood pressure to the vital sign
        this.oxygenLevel = oxygenLevel; // Assigning oxygen level to the vital sign
        this.temperature = temperature; // Assigning body temperature to the vital sign
    }

    // Method to get the heart rate of the patient
    public float getHeartRate() {
        return heartRate; // Returning the heart rate
    }

    // Method to get the blood pressure of the patient
    public float getBloodPressure() {
        return bloodPressure; // Returning the blood pressure
    }

    // Method to get the oxygen level of the patient
    public float getOxygenLevel() {
        return oxygenLevel; // Returning the oxygen level
    }

    // Method to get the body temperature of the patient
    public float getTemperature() {
        return temperature; // Returning the body temperature
    }

    @Override
    public String toString() {
        return "Heart Rate (BPM): " + getHeartRate() + "\n" + // Returning heart rate as part of the vital signs
                "Blood Pressure (Systolic mmHg): " + getBloodPressure() + "\n" + // Returning blood pressure
                "Oxygen Level (%): " + getOxygenLevel() + "\n" + // Returning oxygen level
                "Temperature (Fahrenheit): " + getTemperature(); // Returning body temperature
    }
}
