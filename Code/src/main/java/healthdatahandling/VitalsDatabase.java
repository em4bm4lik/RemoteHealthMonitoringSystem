package healthdatahandling;

import java.util.HashMap;

public class VitalsDatabase {
    private static HashMap<Integer, VitalSign> patientVitals = new HashMap<>(); // HashMap to store patient vitals with patient ID as key

    // Method to add vitals for a patient
    public static void addPatientVitals(int patientId, float heartRate,
                                        float bloodPressure, float oxygenLevel,
                                        float temperature) {
        // Storing the new VitalSign object in the patientVitals map with patientId as the key
        patientVitals.put(patientId, new VitalSign(heartRate, bloodPressure, oxygenLevel, temperature));
    }

    // Method to get the heart rate of a specific patient by patient ID
    public static float getHeartRate(int patientId) {
        return patientVitals.get(patientId).getHeartRate(); // Returning the heart rate for the given patient
    }

    // Method to get the blood pressure of a specific patient by patient ID
    public static float getBloodPressure(int patientId) {
        return patientVitals.get(patientId).getBloodPressure(); // Returning the blood pressure for the given patient
    }

    // Method to get the oxygen level of a specific patient by patient ID
    public static float getOxygenLevel(int patientId) {
        return patientVitals.get(patientId).getOxygenLevel(); // Returning the oxygen level for the given patient
    }

    // Method to get the temperature of a specific patient by patient ID
    public static float getTemperature(int patientId) {
        return patientVitals.get(patientId).getTemperature(); // Returning the temperature for the given patient
    }

    // Method to get all the vital signs of a specific patient by patient ID
    public static String getAllVitals(int patientId) {
        return patientVitals.get(patientId).toString(); // Returning the full string representation of vital signs for the patient
    }
}
