package appointmentscheduling;

import filedatahandling.Database;
import usermanagement.Doctor;
import usermanagement.Patient;

public class AppointmentManager {

    public static void requestFromDoctor(int doctorId, Patient patient) {
        Doctor doctor = Database.getDoctors().get(doctorId); // Getting the doctor object using the provided doctorId
        doctor.addAppointmentRequest(new Appointment(doctor, patient)); // Adding the new appointment request to the doctor's list
    }

    public static void finalizeAppointment(Appointment appointment) {
        appointment.getPatient().addNewAppointment(appointment); // Adding the appointment to the patient's list of appointments
        appointment.setStatus("Pending"); // Updating the status of the appointment to "Pending"
    }

    public static void finalizeCancellation(Appointment appointment) {
        appointment.getDoctor().removeAppointment(appointment); // Removing the cancelled appointment from the doctor's schedule
        appointment.setStatus("Cancelled"); // Setting the appointment status to "Cancelled"
    }

    public static void finalizeRejection(Appointment appointment) {
        appointment.setStatus("Rejected"); // Setting the appointment status to "Rejected"
    }
}

