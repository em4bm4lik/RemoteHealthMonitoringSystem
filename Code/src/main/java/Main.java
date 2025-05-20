import appointmentscheduling.Appointment;
import filedatahandling.Database;
import filedatahandling.Serializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import usermanagement.*;

import java.util.HashMap;

public class Main extends Application {

    public static void main(String[] args) {
        // By default, there will be 3 doctors, 3 patients and 3 admins initialized
        Serializer.readFromFile();
        System.out.println(Database.getPatients());
        System.out.println(Database.getDoctors());
        System.out.println(Database.getAdministrators());
        User.setTemp(getLatestUserID());
        Appointment.setTemp(getLatestAppointmentID());
        LogEntry.setTemp(getLatestLogEntryID());

//       // Creating 3 administrators and registering them
//        Administrator admin1 = new Administrator("abc@gmail.com", "0000", "Admin1", "0300-0000000");
//        Administrator admin2 = new Administrator("def@gmail.com", "0000", "Admin2", "0300-0000000");
//        Administrator admin3 = new Administrator("ghi@gmail.com", "0000", "Admin3", "0300-0000000");
//
//        // Creating 3 doctors and registering them
//        Doctor doctor1 = new Doctor("jkl@gmail.com", "0000", "Doctor1", "0300-0000000");
//        Doctor doctor2 = new Doctor("mno@gmail.com", "0000", "Doctor2", "0300-0000000");
//        Doctor doctor3 = new Doctor("pqr@gmail.com", "0000", "Doctor3", "0300-0000000");
//
//        // Creating 3 patients and registering them
//        Patient patient1 = new Patient("stu@gmail.com", "0000", "Patient1", "0300-0000000");
//        Patient patient2 = new Patient("vwx@gmail.com", "0000", "Patient2", "0300-0000000");
//        Patient patient3 = new Patient("yza@gmail.com", "0000", "Patient3", "0300-0000000");
//
//        Database.getAdministrators().put(admin1.getId(), admin1);
//        Database.getAdministrators().put(admin2.getId(), admin2);
//        Database.getAdministrators().put(admin3.getId(), admin3);
//
//        Database.getDoctors().put(doctor1.getId(), doctor1);
//        Database.getDoctors().put(doctor2.getId(), doctor2);
//        Database.getDoctors().put(doctor3.getId(), doctor3);
//
//        Database.getPatients().put(patient1.getId(), patient1);
//        Database.getPatients().put(patient2.getId(), patient2);
//        Database.getPatients().put(patient3.getId(), patient3);

        // Launching the JavaFX UI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Remote Health Monitoring System");

        Button signupAdmin   = new Button("Signup as Administrator");
        Button loginAdmin    = new Button("Login as Administrator");
        Button signupDoctor  = new Button("Signup as Doctor");
        Button loginDoctor   = new Button("Login as Doctor");
        Button signupPatient = new Button("Signup as Patient");
        Button loginPatient  = new Button("Login as Patient");
        Button exitApp       = new Button("Exit");

        signupAdmin.setOnAction(e -> SignupLoginService.signup("Administrator"));
        loginAdmin.setOnAction(e -> SignupLoginService.login ("Administrator"));
        signupDoctor.setOnAction(e -> SignupLoginService.signup("Doctor"));
        loginDoctor.setOnAction(e -> SignupLoginService.login ("Doctor"));
        signupPatient.setOnAction(e -> SignupLoginService.signup("Patient"));
        loginPatient.setOnAction(e -> SignupLoginService.login ("Patient"));

        exitApp.setOnAction(e -> {
            Serializer.writeToFile();
            primaryStage.close();
        });

        // Layout
        VBox root = new VBox(10,
                signupAdmin, loginAdmin,
                signupDoctor, loginDoctor,
                signupPatient, loginPatient,
                exitApp
        );
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Ensures data is saved if the window is closed via the window controls
        Serializer.writeToFile();
    }

    private static int getLatestUserID() {
        int max = 0;

        for (Patient patient : Database.getPatients().values()) {
            max = Math.max(max, patient.getId());
        }

        for (Doctor doctor : Database.getDoctors().values()) {
            max = Math.max(max, doctor.getId());
        }

        for (Administrator administrator : Database.getAdministrators().values()) {
            max = Math.max(max, administrator.getId());
        }

        return max;

    }

    private static int getLatestAppointmentID() {
        int max = 0;

        for (Patient p : Database.getPatients().values()) {
            HashMap<Integer, Appointment> appointments = p.getAppointments();
            if (appointments != null) {
                for (Appointment appointment : appointments.values()) {
                    max = Math.max(max, appointment.getId());
                }
            }
        }

        for (Doctor d: Database.getDoctors().values()) {
            if (d.getAppointmentRequests() != null) {
                for (Appointment request : d.getAppointmentRequests().values())
                    max = Math.max(max, request.getId());
            }
        }

        return max;
    }

    private static int getLatestLogEntryID() {
        int max = 0;
        for (LogEntry log : Database.getLogs().values()) {
            max = Math.max(max, log.getLogId());
        }
        return max;
    }

}
