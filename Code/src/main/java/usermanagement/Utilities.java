package usermanagement;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utilities {

    private static Scanner scanner = new Scanner(System.in);
    public static String collectionToString(Collection collection, String type) {
        int count = 0;
        StringBuilder builder = new StringBuilder(); // Creating a string builder to store formatted result

        for (Object entry : collection)
            builder.append(type)                      // Appending the type label (e.g., "Appointment", "Doctor")
                    .append(" ")
                    .append(++count)                   // Adding a count number for each entry
                    .append(":\n")
                    .append(entry.toString())          // Adding the string representation of the object
                    .append("\n\n");

        return builder.toString(); // Returning the final formatted string
    }

    public static int readInt() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    public static float readFloat() {
        while (true) {
            try {
                return scanner.nextFloat();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid floating point number.");
                scanner.nextLine();
            }
        }
    }

}
