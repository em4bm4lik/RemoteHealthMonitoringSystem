package usermanagement;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogEntry implements Serializable {
    private static int temp; // Static variable to generate unique log IDs
    private int logId; // The ID of the log entry
    private String logType; // The type/category of the log (e.g., 'Appointment Request')
    private String details;  // Detailed description of the log entry
    private LocalDateTime timestamp; // The timestamp when the log entry was created

    // Constructor to create a new log entry
    public LogEntry(String logType, String details) {
        this.logId = ++temp; // Increment the static temp variable to generate a unique log ID
        this.logType = logType; // Set the log type
        this.details = details; // Set the log details
        this.timestamp = LocalDateTime.now(); // Set the timestamp to the current time
    }

    // Getter for log ID
    public int getLogId() {
        return logId;
    }

    // Getter for log type
    public String getLogType() {
        return logType;
    }

    // Getter for log details
    public String getDetails() {
        return details;
    }

    // Getter for timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public static void setTemp(int temp) {
        LogEntry.temp = temp;
    }

    // Override toString to return a string representation of the log entry
    @Override
    public String toString() {
        return "Log ID: " + getLogId() + "\n" +
                "Log Type: " + getLogType() + "\n" +
                "Log Details: " + getDetails() + "\n" +
                "Log Time: " + getTimestamp().toString(); // Format the log's timestamp as a string
    }
}
