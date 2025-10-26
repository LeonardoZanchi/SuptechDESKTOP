package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Usuario;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * Classe responsável por gerenciar a seleção de usuários na tabela
 * Controla o estado dos botões baseado na seleção
 * Aplica o princípio Single Responsibility (SRP)
 */
public class UserSelectionManager {
    
    private final TableView<Usuario> tableView;
    private final Button btnInformacoes;
    private final Button btnEditar;
    private final Button btnExcluir;

    public UserSelectionManager(
            TableView<Usuario> tableView,
            Button btnInformacoes,
            Button btnEditar,
            Button btnExcluir) {
        
        this.tableView = tableView;
        this.btnInformacoes = btnInformacoes;
        this.btnEditar = btnEditar;
        this.btnExcluir = btnExcluir;
    }

    /**
     * Configura o comportamento dos botões baseado na seleção
     */
    public void configurarSelecao() {
        // Estado inicial - botões desabilitados
        desabilitarBotoesAcao();

        // Listener para habilitar/desabilitar botões conforme seleção
        tableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean usuarioSelecionado = newSelection != null;
                habilitarBotoesAcao(usuarioSelecionado);
            }
        );
    }

    /**
     * Desabilita todos os botões de ação
     */
    private void desabilitarBotoesAcao() {
        btnInformacoes.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    /**
     * Habilita ou desabilita os botões de ação
     */
    private void habilitarBotoesAcao(boolean habilitar) {
        btnInformacoes.setDisable(!habilitar);
        btnEditar.setDisable(!habilitar);
        btnExcluir.setDisable(!habilitar);
    }

    /**
     * Retorna o usuário atualmente selecionado
     */
    public Usuario getUsuarioSelecionado() {
        return tableView.getSelectionModel().getSelectedItem();
    }
}
