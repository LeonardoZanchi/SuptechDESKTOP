package br.com.suptec.controllers.components;

import br.com.suptec.core.SceneManager;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller do grid de menu principal. Expõe handlers para cada card do menu
 * que carregam as telas correspondentes (Usuários, Chamados, Relatórios e
 * Configurações).
 */
public class MenuGridController {

    /**
     * Handler acionado ao clicar no card "Usuários". Carrega a view de lista de
     * usuários mantendo as dimensões do Stage principal.
     */
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

    /**
     * Handler acionado ao clicar no card "Chamados". Abre a lista de chamados
     * na mesma janela mantendo o tamanho atual do Stage.
     */
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

    /**
     * Handler acionado ao clicar no card "Relatórios". Substitui a cena atual
     * pela view de relatórios.
     */
    @FXML
    private void handleRelatorios(javafx.scene.input.MouseEvent event) {
        try {
            // Obter o Stage atual através do evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Carregar a tela de relatórios
            SceneManager.loadScene(stage, "/fxml/ReportsView.fxml", "SUPTEC - Relatórios", 1600, 1000, false, false);
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de relatórios: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError("Erro", "Não foi possível abrir a tela de relatórios.");
        }
    }

    /**
     * Handler acionado ao clicar em "Configurações". Usa um método que
     * substitui apenas o root da cena para preservar o tamanho do Stage e
     * evitar redimensionamentos indesejados.
     */
    @FXML
    private void handleConfiguracoes(MouseEvent event) {
        try {
            // Obter o Stage atual através do evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Replace root to preserve Stage size (avoid window resizing)
        SceneManager.replaceRootPreserveStage(stage, "/fxml/ConfigView.fxml", "SUPTEC - Configurações");
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de configurações: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError("Erro", "Não foi possível abrir a tela de configurações.");
        }
    }
}

