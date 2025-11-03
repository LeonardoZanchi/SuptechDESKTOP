package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Chamado;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * Classe responsável por gerenciar a seleção de chamados na tabela
 * Aplica o princípio Single Responsibility (SRP)
 */
public class ChamadoSelectionManager {
    
    private final TableView<Chamado> tableView;
    private final Button btnInformacoes;
    private final Button btnEditar;
    private final Button btnExcluir;

    public ChamadoSelectionManager(
            TableView<Chamado> tableView,
            Button btnInformacoes,
            Button btnEditar,
            Button btnExcluir) {
        
        this.tableView = tableView;
        this.btnInformacoes = btnInformacoes;
        this.btnEditar = btnEditar;
        this.btnExcluir = btnExcluir;
    }

    public void configurarSelecao() {
        desabilitarBotoesAcao();

        tableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean chamadoSelecionado = newSelection != null;
                habilitarBotoesAcao(chamadoSelecionado);
            }
        );
    }

    private void desabilitarBotoesAcao() {
        btnInformacoes.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private void habilitarBotoesAcao(boolean habilitar) {
        btnInformacoes.setDisable(!habilitar);
        btnEditar.setDisable(!habilitar);
        btnExcluir.setDisable(!habilitar);
    }

    public Chamado getChamadoSelecionado() {
        return tableView.getSelectionModel().getSelectedItem();
    }
}
