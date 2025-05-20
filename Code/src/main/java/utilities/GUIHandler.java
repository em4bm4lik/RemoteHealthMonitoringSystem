package utilities;

import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.Optional;

public class GUIHandler {
    public static String prompt(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        Optional<String> result = dialog.showAndWait();
        return result.orElse("<");
    }

    // Helper to replace System.out.println for errors/info
    public static void show(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);

        // 1. Create a TextArea with your message
        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // 2. Give it a preferred size (so scroll bars appear when needed)
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(200);

        // 3. Put it in the Alert’s content pane
        alert.getDialogPane().setContent(textArea);

        // 4. Allow the Alert to resize to our preferred size
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        // 5. Show and wait
        alert.showAndWait();
    }
}
