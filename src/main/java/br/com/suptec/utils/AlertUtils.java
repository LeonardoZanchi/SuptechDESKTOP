package br.com.suptec.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Utilitário para exibir caixas de diálogo (alerts) padronizadas na aplicação.
 * Centraliza a criação de Alerts para garantir um visual consistente e permitir
 * adicionar o ícone da aplicação automaticamente.
 *
 * Todos os métodos são estáticos e seguros para uso direto de controllers e
 * services.
 */
public final class AlertUtils {

    private AlertUtils() {}

    /**
     * Exibe uma caixa de informação (OK)
     */
    public static void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    /**
     * Exibe uma caixa de erro (OK)
     */
    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    /**
     * Exibe um alerta de aviso (OK)
     */
    public static void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    /**
     * Exibe uma mensagem de sucesso (reutiliza INFORMATION)
     */
    public static void showSuccess(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    /**
     * Exibe uma caixa de confirmação e retorna true se o usuário confirmar
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Adiciona o ícone da aplicação ao dialog (se possível)
        addIconToAlert(alert);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Cria e exibe um Alert genérico com o tipo passado (INFORMATION, ERROR, etc.)
     */
    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Adiciona o ícone da aplicação ao dialog (se possível)
        addIconToAlert(alert);
        
        alert.showAndWait();
    }

    /**
     * Tenta adicionar o ícone da aplicação na janela do Alert. Em alguns
     * ambientes o carregamento da imagem pode falhar (resources não encontrados),
     * por isso o código está protegido por try/catch.
     */
    private static void addIconToAlert(Alert alert) {
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image icon = new Image(AlertUtils.class.getResourceAsStream("/images/LogoPrincipal.jpg"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            // Não estourar exceção para a UI; logamos para depuração apenas
            System.err.println("Erro ao carregar logo no alert: " + e.getMessage());
        }
    }
}
