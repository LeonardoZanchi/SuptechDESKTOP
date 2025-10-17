package br.com.suptec.controllers.components;

import java.util.Optional;

import br.com.suptec.core.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HeaderController {

    @FXML 
    private Label lblUsuarioLogado;

    @FXML
    public void initialize() {
        lblUsuarioLogado.setText("Usuário: Admin");
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setHeaderText(null);
        alert.setContentText("Deseja sair?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) lblUsuarioLogado.getScene().getWindow();
            SceneManager.loadScene(stage, "/fxml/LoginView.fxml", 
                "SUPTEC - Sistema De Chamados Técnicos");
        }
    }
}
