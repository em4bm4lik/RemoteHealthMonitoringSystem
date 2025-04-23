package emergencyalertsystem;

import usermanagement.Doctor;
import usermanagement.Patient;

import java.util.Scanner;

public class PanicButton {
    public void press(Patient patient) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Clearing Buffer
        System.out.print("Enter a reason for your panic press: ");
        String reason = scanner.nextLine();
        for (Doctor doctor : patient.getDoctors().values()) {
            EmergencyAlert newAlert = new EmergencyAlert(patient, doctor, reason);
            newAlert.trigger();
        }
    }
 }
