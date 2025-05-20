package healthdatahandling;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import utilities.GUIHandler;
import utilities.Utilities;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VitalsDatabase {
    private static HashMap<Integer, TreeMap<LocalDate, VitalSign>> patientVitals = new HashMap<>(); // HashMap to store patient vitals with patient ID as key

    // Method to add vitals for a patient
    public static void addPatientVitals(int patientId, float heartRate,
                                        float bloodPressure, float oxygenLevel,
                                        float temperature) {
        // Storing the new VitalSign object in the patientVitals map with patientId as the key
        VitalSign vitals =  new VitalSign(heartRate, bloodPressure, oxygenLevel, temperature);
        if (!patientVitals.containsKey(patientId)) {
            TreeMap<LocalDate, VitalSign> datedVitals = new TreeMap<>();
            datedVitals.put(LocalDate.now(), vitals);
            patientVitals.put(patientId, datedVitals);
        } else
            patientVitals.get(patientId).put(LocalDate.now(), vitals);
    }

    // Method to get the heart rate of a specific patient by patient ID
    public static String getHeartRates(int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        ArrayList<String> formattedHeartRates = new ArrayList<>();

        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet())
            formattedHeartRates.add(String.format("%s -> %.1f", entry.getKey(), entry.getValue().getHeartRate()));


        return Utilities.collectionToString(formattedHeartRates, "Heart Rate");
    }

    // Method to get the blood pressure of a specific patient by patient ID
    public static String getBloodPressures(int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        ArrayList<String> formattedBloodPressures = new ArrayList<>();

        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet())
            formattedBloodPressures.add(String.format("%s -> %.1f", entry.getKey(), entry.getValue().getBloodPressure()));

        return Utilities.collectionToString(formattedBloodPressures, "Blood Pressure");
    }

    // Method to get the oxygen level of a specific patient by patient ID
    public static String getOxygenLevels(int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        ArrayList<String> formattedOxygenLevels = new ArrayList<>();

        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet())
            formattedOxygenLevels.add(String.format("%s -> %.1f", entry.getKey(), entry.getValue().getOxygenLevel()));

        return Utilities.collectionToString(formattedOxygenLevels, "Oxygen Level");
    }

    // Method to get the temperature of a specific patient by patient ID
    public static String getTemperatures(int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        ArrayList<String> formattedTemperatures = new ArrayList<>();

        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet())
            formattedTemperatures.add(String.format("%s -> %.1f", entry.getKey(), entry.getValue().getTemperature()));

        return Utilities.collectionToString(formattedTemperatures, "Temperature");
    }

    public static String getAllVitals (int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        ArrayList<String> formattedVitals = new ArrayList<>();
        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet()) {
            LocalDate date = entry.getKey();
            VitalSign v = entry.getValue();
            formattedVitals.add(String.format(
                    "%s -> Heart Rate: %.1f, Blood Pressure: %.1f, Oxygen Level: %.1f, Temperature: %.1f",
                    date, v.getHeartRate(), v.getBloodPressure(), v.getOxygenLevel(), v.getTemperature()
            ));
        }

        return Utilities.collectionToString(formattedVitals, "Vitals");
    }

    public static JFreeChart generateHealthTrendGraph(int patientId) {
        TreeMap<LocalDate, VitalSign> datedVitals = patientVitals.get(patientId);
        if (datedVitals == null || datedVitals.isEmpty()) {
            GUIHandler.show("No vitals found for patient ID: " + patientId);
            return ChartFactory.createTimeSeriesChart("", "", "", new TimeSeriesCollection(), false, false, false);
        }

        TimeSeries heartRateSeries = new TimeSeries("Heart Rate");
        TimeSeries bpSeries = new TimeSeries("Blood Pressure");
        TimeSeries oxygenSeries = new TimeSeries("Oxygen Level");
        TimeSeries temperatureSeries = new TimeSeries("Temperature");

        for (Map.Entry<LocalDate, VitalSign> entry : datedVitals.entrySet()) {
            LocalDate date = entry.getKey();
            VitalSign v = entry.getValue();

            // Converting to Day format
            Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());

            // Using a try-catch to avoid duplicate date errors (TimeSeries doesn't allow same day repeated)
            try {
                heartRateSeries.add(day, v.getHeartRate());
                bpSeries.add(day, v.getBloodPressure());
                oxygenSeries.add(day, v.getOxygenLevel());
                temperatureSeries.add(day, v.getTemperature());
            } catch (Exception e) {
                GUIHandler.show("Skipping duplicate or invalid date: " + date);
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(heartRateSeries);
        dataset.addSeries(bpSeries);
        dataset.addSeries(oxygenSeries);
        dataset.addSeries(temperatureSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Health Trend for Patient ID: " + patientId,
                "Date",
                "Vital Values",
                dataset,
                true,
                true,
                false
        );

        // Customize plot for shapes and value markers
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesShapesVisible(i, true);         // Showing data points
            renderer.setSeriesShapesFilled(i, true);          // Filling the shape (e.g., dot)
            renderer.setSeriesItemLabelsVisible(i, true);     // Showing value labels
            renderer.setSeriesPaint(i, getSeriesColor(i));    // Assigning a different color to each
        }

        renderer.setDefaultItemLabelGenerator(new StandardXYItemLabelGenerator());
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);

        return chart;
    }

    public static void plotChart(JFreeChart chart) {
        ChartFrame frame = new ChartFrame("Health Trend Graph", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private static Color getSeriesColor(int index) {
        return switch (index) {
            case 0 -> Color.RED;        // Heart rate
            case 1 -> Color.BLUE;       // BP
            case 2 -> Color.GREEN;      // Oxygen
            case 3 -> Color.ORANGE;     // Temp
            default -> Color.BLACK;
        };
    }


    public static void setPatientVitals(HashMap<Integer, TreeMap<LocalDate, VitalSign>> patientVitals) {
        VitalsDatabase.patientVitals = patientVitals;
    }

    public static HashMap<Integer, TreeMap<LocalDate, VitalSign>> getPatientVitals() {
        return patientVitals;
    }
}
