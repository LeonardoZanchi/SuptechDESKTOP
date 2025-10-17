package br.com.suptec.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class AlertUtils {

    private AlertUtils() {}

    public static void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Adicionar logo ao ícone da janela
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image icon = new Image(AlertUtils.class.getResourceAsStream("/images/LogoPrincipal.jpg"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo no alert: " + e.getMessage());
        }
        
        alert.showAndWait();
    }
}
