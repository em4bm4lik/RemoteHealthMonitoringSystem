package emergencyalertsystem;

import usermanagement.Doctor;
import usermanagement.Patient;
import java.time.LocalTime;

public class EmergencyAlert {
    private Patient patient;
    private Doctor doctor;
    private String reason;
    private LocalTime timestamp;

    public EmergencyAlert(Patient patient, Doctor doctor, String reason) {
        this.patient = patient;
        this.doctor = doctor;
        this.reason = reason;
        timestamp = LocalTime.now();
    }

    public void trigger() {
        NotificationService notificationService = new NotificationService();
        notificationService.sendNotifications(doctor, toString());
    }

    @Override
    public String toString() {
        return "EMERGENCY CALL! " + "\n" +
                "Respected Dr. " + doctor.getName() + "\n" +
                "Your patient " + patient.getName() + " has an emergency! Patient details are as follows: " + "\n" +
                patient.toString() + "\n" +
                "Reason of emergency: " + reason + "\n" +
                "*** This alert was generated on " + timestamp + " ***";
    }
}
