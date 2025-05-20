package healthdatahandling;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import usermanagement.Patient;
import utilities.GUIHandler;

import java.io.ByteArrayOutputStream;

public class ReportGenerator {

    public static void generateReport(Patient patient) {
        try {
            String outputPath = "C:\\Users\\hp\\Downloads\\report1.pdf";
            // Header
            String name = patient.getName();
            int id = patient.getId();

            // Vitals & Medical History strings
            String vitalsHistory = VitalsDatabase.getAllVitals(id);
            String medicalHistory = patient.getMedicalHistory().toString();

            // Building chart and rendering to PNG bytes
            JFreeChart chart = VitalsDatabase.generateHealthTrendGraph(id);
            ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(pngOut, chart, 600, 300);
            Image chartImage = new Image(ImageDataFactory.create(pngOut.toByteArray()))
                    .scaleToFit(500, 250);

            // Creating PDF
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            // Header
            doc.add(new Paragraph("Patient Report").setBold().setFontSize(16));
            doc.add(new Paragraph("Name: " + name));
            doc.add(new Paragraph("ID:   " + id));
            doc.add(new Paragraph("\n"));

            // Vitals History
            doc.add(new Paragraph("Vitals History").setBold());
            doc.add(new Paragraph(vitalsHistory));

            // Medical History
            doc.add(new Paragraph("Medical History").setBold());
            doc.add(new Paragraph(medicalHistory));

            // Health‑Trend Graph
            doc.add(new Paragraph("Health Trend & Graph").setBold());
            doc.add(chartImage);

            doc.close();
        } catch (Exception e) {
            GUIHandler.show("Failed to generate report for patient ID "
                    + patient.getId() + ": " + e.getMessage());
        }
    }
}


