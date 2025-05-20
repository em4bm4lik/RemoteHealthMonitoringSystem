package utilities;

import java.util.Collection;

public class Utilities {

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
}
