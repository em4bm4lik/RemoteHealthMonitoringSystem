package emergencyalertsystem;

import usermanagement.Doctor;
import usermanagement.Patient;
import utilities.GUIHandler;

import java.io.Serializable;
import java.util.Scanner;

public class PanicButton implements Serializable {
    public void press(Patient patient) {
        String reason = GUIHandler.prompt("Enter a reason for your panic press: ");
        for (Doctor doctor : patient.getDoctors().values()) {
            EmergencyAlert newAlert = new EmergencyAlert(patient, doctor, reason);
            newAlert.trigger();
        }
    }
 }
