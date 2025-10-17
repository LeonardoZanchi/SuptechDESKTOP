package br.com.suptec.controllers.components;

import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MenuGridController {

    @FXML
    private void handleUsuarios(MouseEvent event) {
        AlertUtils.showInfo("Usuários", "Módulo em desenvolvimento.");
    }

    @FXML
    private void handleChamados(MouseEvent event) {
        AlertUtils.showInfo("Chamados", "Módulo em desenvolvimento.");
    }

    @FXML
    private void handleRelatorios(MouseEvent event) {
        AlertUtils.showInfo("Relatórios", "Módulo em desenvolvimento.");
    }

    @FXML
    private void handleConfiguracoes(MouseEvent event) {
        AlertUtils.showInfo("Configurações", "Módulo em desenvolvimento.");
    }
}
