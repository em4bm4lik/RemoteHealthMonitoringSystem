import filedatahandling.Database;
import usermanagement.Administrator;
import usermanagement.Doctor;
import usermanagement.Patient;
import utilities.GUIHandler;

import java.util.ArrayList;
import static utilities.GUIHandler.*;

public class SignupLoginService {

    public static void signup(String role) {
        String email, password, name, phone;
        String input;

        if (role.equals("Administrator"))
            do {
                input = prompt("Enter the secret platform phrase to signup as admin (or '<' to go back):");
                if (input.equals("<")) return;
                if (!input.equals("Winter is Coming!"))
                    show("Incorrect Phrase!");
            } while(!input.equals("Winter is Coming!"));

        do {
            input = prompt("Enter your email (or '<' to go back):");
            if (input.equals("<")) return;
            if (getRoleEmails(role).contains(input))
                show("Email already registered as " + role);
        } while (getRoleEmails(role).contains(input));
        email = input;

        do {
            input = prompt("Enter your password [Min length 4] (or '<' to go back):");
            if (input.equals("<")) return;
            if (input.length() < 4)
                show("Password must be at least 4 characters long");
        } while (input.length() < 4);
        password = input;

        input = prompt("Enter your name (or '<' to go back):");
        if (input.equals("<")) return;
        name = input;

        input = prompt("Enter your phone (or '<' to go back):");
        if (input.equals("<")) return;
        phone = input;

        addRole(role, email, password, name, phone);
        GUIHandler.show("Signup successful!");
    }

    public static void login(String role) {
        String email, password;
        String input;

        do {
            input = prompt("Enter your email (or '<' to go back):");
            if (input.equals("<")) return;
            if (!getRoleEmails(role).contains(input))
                show("Email not registered as " + role + "!");
        } while (!getRoleEmails(role).contains(input));
        email = input;

        do {
            input = prompt("Enter your password (or '<' to go back):");
            if (input.equals("<")) return;
            if (!input.equals(getRolePasswordByEmail(role, email)))
                show("Password Incorrect!");
        } while (!input.equals(getRolePasswordByEmail(role, email)));
        password = input;

        getRoleOptions(role, email);
    }

    private static ArrayList<String> getRoleEmails(String role) {
        ArrayList<String> roleEmails = new ArrayList<>();
        if (role.equals("Administrator"))
            for (Administrator admin : Database.getAdministrators().values())
                roleEmails.add(admin.getEmail());

        else if (role.equals("Doctor"))
            for (Doctor doctor : Database.getDoctors().values())
                roleEmails.add(doctor.getEmail());

        else if (role.equals("Patient"))
            for (Patient patient : Database.getPatients().values())
                roleEmails.add(patient.getEmail());

        return roleEmails;
    }

    private static void addRole(String role, String email, String password, String name, String phone) {
        if (role.equals("Administrator")) {
            Administrator newAdmin = new Administrator(email, password, name, phone);
            Database.getAdministrators().put(newAdmin.getId(), newAdmin);
        }
        else if (role.equals("Doctor")) {
            Doctor newDoctor = new Doctor(email, password, name, phone);
            Database.getDoctors().put(newDoctor.getId(), newDoctor);
        }
        else if (role.equals("Patient")) {
            Patient newPatient = new Patient(email, password, name, phone);
            Database.getPatients().put(newPatient.getId(), newPatient);
        }
    }

    private static String getRolePasswordByEmail(String role, String email) {
        String password = "";

        if (role.equals("Administrator")) {
            for (Administrator admin : Database.getAdministrators().values())
                if (admin.getEmail().equals(email))
                    password = admin.getPassword();
        }

        else if (role.equals("Doctor")) {
            for (Doctor doctor : Database.getDoctors().values())
                if (doctor.getEmail().equals(email))
                    password = doctor.getPassword();
        }

        else if (role.equals("Patient")) {
            for (Patient patient : Database.getPatients().values())
                if (patient.getEmail().equals(email))
                    password = patient.getPassword();
        }

        return password;
    }

    private static void getRoleOptions(String role, String email) {
        if (role.equals("Administrator")) {
            for (Administrator admin : Database.getAdministrators().values())
                if (admin.getEmail().equals(email))
                    admin.getOptions();
        }

        else if (role.equals("Doctor")) {
            for (Doctor doctor : Database.getDoctors().values())
                if (doctor.getEmail().equals(email))
                    doctor.getOptions();
        }

        else if (role.equals("Patient")) {
            for (Patient patient : Database.getPatients().values())
                if (patient.getEmail().equals(email))
                    patient.getOptions();
        }
    }
}
