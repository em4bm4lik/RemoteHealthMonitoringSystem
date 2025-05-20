package filedatahandling;

import usermanagement.Administrator;
import usermanagement.Doctor;
import usermanagement.LogEntry;
import usermanagement.Patient;

import java.util.HashMap;

public class Database {
    private static HashMap<Integer, Administrator> administrators = new HashMap<>(); // Holds all administrators
    private static HashMap<Integer, LogEntry> logs = new HashMap<>(); // Holds all logs in the system
    private static HashMap<Integer, Doctor> doctors = new HashMap<>(); // Holds all doctors in the system
    private static HashMap<Integer, Patient> patients = new HashMap<>(); // Holds all patients in the system


    // Getter methods for administrators, doctors, and patients HashMaps
    public static HashMap<Integer, Administrator> getAdministrators() {
        return administrators;
    }

    public static HashMap<Integer, Doctor> getDoctors() {
        return doctors;
    }

    public static HashMap<Integer, Patient> getPatients() {
        return patients;
    }

    public static HashMap<Integer, LogEntry> getLogs() {
        return logs;
    }

    public static void setAdministrators(HashMap<Integer, Administrator> administrators) {
        Database.administrators = administrators;
    }

    public static void setLogs(HashMap<Integer, LogEntry> logs) {
        Database.logs = logs;
    }

    public static void setDoctors(HashMap<Integer, Doctor> doctors) {
        Database.doctors = doctors;
    }

    public static void setPatients(HashMap<Integer, Patient> patients) {
        Database.patients = patients;
    }
}
