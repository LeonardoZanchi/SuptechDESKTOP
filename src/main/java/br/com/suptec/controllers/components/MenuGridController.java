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
    private void handleChamados(MouseEvent event) {
        try {
            System.out.println("=== Abrindo tela de chamados ===");
            // Obter o Stage atual através do evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            System.out.println("Stage obtido: " + stage);
            // Carregar a tela de lista de chamados com as mesmas dimensões do menu principal
            SceneManager.loadScene(stage, "/fxml/ChamadoListView.fxml", "SUPTEC - Gerenciamento de Chamados", 1600, 1000, false, false);
            System.out.println("Tela carregada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de chamados: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError("Erro", "Nao foi possivel abrir a tela de chamados.");
        }
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

