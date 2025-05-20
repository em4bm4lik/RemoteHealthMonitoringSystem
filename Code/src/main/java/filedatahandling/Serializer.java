package filedatahandling;
import chatandvideoconsultation.ChatServer;
import healthdatahandling.VitalsDatabase;

import java.io.*;
import java.util.HashMap;

public class Serializer {

    public static void readFromFile() {
        try {
            Database.setPatients(readObject("patients.txt"));
            Database.setDoctors(readObject("doctors.txt"));
            Database.setAdministrators(readObject("administrators.txt"));
            Database.setLogs(readObject("logs.txt"));
            ChatServer.setChats(readObject("chats.txt"));
            VitalsDatabase.setPatientVitals((readObject("vitals.txt")));
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Database.setPatients(new HashMap<>());
            Database.setDoctors(new HashMap<>());
            Database.setAdministrators(new HashMap<>());
            Database.setLogs(new HashMap<>());
        }
    }

    public static void writeToFile() {
        try {
            writeObject("patients.txt", Database.getPatients());
            writeObject("doctors.txt", Database.getDoctors());
            writeObject("administrators.txt", Database.getAdministrators());
            writeObject("logs.txt", Database.getLogs());
            writeObject("chats.txt", ChatServer.getChats());
            writeObject("vitals.txt", VitalsDatabase.getPatientVitals());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <K, V> HashMap<K, V> readObject(String filePath)
            throws IOException, ClassNotFoundException
    {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis))
        {
            return (HashMap<K, V>) ois.readObject();
        }
    }

    private static <K, V> void writeObject(String filePath, HashMap<K, V> map)
            throws IOException
    {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(map);
        }
    }
}
