package br.com.suptec.controllers.components;

import java.util.Optional;

import br.com.suptec.core.SceneManager;
import br.com.suptec.services.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HeaderController {

    @FXML 
    private Label lblUsuarioLogado;

    @FXML
    private ImageView imgLogo;

    @FXML
    public void initialize() {
        // Obter o nome do usuário logado do serviço
        AuthService authService = AuthService.getInstance();
        if (authService != null) {
            String nomeUsuario = authService.getNomeUsuarioLogado();
            lblUsuarioLogado.setText("Usuário: " + nomeUsuario);
        } else {
            lblUsuarioLogado.setText("Usuário: Admin");
        }
        
        // Carregar a logo da aplicação
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/LogoSemFundo.png"));
            imgLogo.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo do header: " + e.getMessage());
            // A imagem não será exibida, mas o programa continua funcionando
        }
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
