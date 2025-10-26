package br.com.suptec.controllers.components;

import br.com.suptec.core.SceneManager;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuGridController {

    @FXML
    private void handleUsuarios(MouseEvent event) {
        try {
            // Obter o Stage atual através do evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Carregar a tela de lista de usuários com as mesmas dimensões do menu principal e sem permitir maximizar
            SceneManager.loadScene(stage, "/fxml/UserListView.fxml", "SUPTEC - Gerenciamento de Usuários", 1600, 1000, true, false);
        } catch (Exception e) {
            System.err.println("✗ Erro ao abrir tela de usuários: " + e.getMessage());
            AlertUtils.showError("Erro", "Não foi possível abrir a tela de usuários.");
        }
    }

    @FXML
    private void handleChamados() {
        AlertUtils.showInfo("Chamados", "Módulo em desenvolvimento.");
    }

    @FXML
    private void handleRelatorios() {
        AlertUtils.showInfo("Relatórios", "Módulo em desenvolvimento.");
    }

    @FXML
    private void handleConfiguracoes() {
        AlertUtils.showInfo("Configurações", "Módulo em desenvolvimento.");
    }
}

