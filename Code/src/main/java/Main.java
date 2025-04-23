import usermanagement.*;

public class Main {
    public static void main(String[] args) {
        // By default, there will be 3 doctors, 3 patients and 3 admins initialized

        // Creating 3 administrators and registering them
        Administrator admin1 = new Administrator("Admin1", "abc@gmail.com", "0300-0000000");
        Administrator admin2 = new Administrator("Admin2", "def@gmail.com", "0300-0000000");
        Administrator admin3 = new Administrator("Admin3", "ghi@gmail.com", "0300-0000000");

        // Creating 3 doctors and registering them
        Doctor doctor1 = new Doctor("Doctor1", "jkl@gmail.com", "0300-0000000");
        Doctor doctor2 = new Doctor("Doctor2", "mno@gmail.com", "0300-0000000");
        Doctor doctor3 = new Doctor("Doctor3", "pqr@gmail.com", "0300-0000000");

        // Creating 3 patients and registering them
        Patient patient1 = new Patient("Patient1", "aimlcv2025@gmail.com", "0300-0000000");
        Patient patient2 = new Patient("Patient2", "vwx@gmail.com", "0300-0000000");
        Patient patient3 = new Patient("Patient3", "yza@gmail.com", "0300-0000000");

        // Informing the user about the default users added to the system
        System.out.println("\n*** By default the following 3 admins, doctors and patients are available in the system only ***\n");
        System.out.println("\nAdministrators:\n" + admin1 + "\n" + admin2 + "\n" + admin3);
        System.out.println("\nDoctors:\n" + doctor1 + "\n" + doctor2 + "\n" + doctor3);
        System.out.println("\nPatients:\n" + patient1 + "\n" + patient2 + "\n" + patient3);

        // Launching the main user interface
        getMainInterface();
    }

    public static void getMainInterface() {
        // Printing system welcome message
        System.out.println("\n-------------------------------------------");
        System.out.println("WELCOME TO REMOTE HEALTH MONITORING SYSTEM:");
        System.out.println("-------------------------------------------\n");

        while (true) {
            // Prompting user to select user type
            System.out.println("Enter 1 to use as administrator");
            System.out.println("Enter 2 to use as doctor");
            System.out.println("Enter 3 to use as patient");
            System.out.println("Enter 0 to terminate program");
            System.out.print("Your Choice: ");
            int input = Utilities.readInt();

            switch (input) {
                case 0:
                    return; // Exiting program
                case 1:
                    useAsAdministrator(); // Calling admin interface
                    break;
                case 2:
                    useAsDoctor(); // Calling doctor interface
                    break;
                case 3:
                    useAsPatient(); // Calling patient interface
                    break;
                default:
                    // Handling invalid input
                    System.out.println("Please enter a valid value");
            }
        }
    }

    public static void useAsAdministrator() {
        int administratorId;
        // Keep asking until a valid admin ID is entered or 0 to go back
        do {
            System.out.print("Enter Administrator ID (or 0 to go back): ");
            administratorId = Utilities.readInt();
            if (administratorId == 0) return; // Going back to previous menu
            if (!Database.getAdministrators().containsKey(administratorId))
                System.out.println("No Administrator has the ID: " + administratorId); // Notifying about invalid ID
        } while (!Database.getAdministrators().containsKey(administratorId));

        // Invoking options for the selected admin
        Database.getAdministrators().get(administratorId).getOptions();
    }

    public static void useAsDoctor() {
        int doctorId;
        // Keep asking until a valid doctor ID is entered or 0 to go back
        do {
            System.out.print("Enter Doctor ID (or 0 to go back): ");
            doctorId = Utilities.readInt();
            if (doctorId == 0) return;
            if (!Database.getDoctors().containsKey(doctorId))
                System.out.println("No Doctor has the ID: " + doctorId);
        } while (!Database.getDoctors().containsKey(doctorId));

        // Invoking options for the selected doctor
        Database.getDoctors().get(doctorId).getOptions();
    }

    public static void useAsPatient() {
        int patientId;
        // Keep asking until a valid patient ID is entered or 0 to go back
        do {
            System.out.print("Enter Patient ID (or 0 to go back): ");
            patientId = Utilities.readInt();
            if (patientId == 0) return;
            if (!Database.getPatients().containsKey(patientId))
                System.out.println("No Patient has the ID: " + patientId);
        } while (!Database.getPatients().containsKey(patientId));

        // Invoking options for the selected patient
        Database.getPatients().get(patientId).getOptions();
    }
}
