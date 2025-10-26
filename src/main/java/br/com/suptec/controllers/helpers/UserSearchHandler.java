package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Usuario;
import br.com.suptec.services.UserManagementService;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Classe responsável por gerenciar a busca e filtragem de usuários
 * Aplica o princípio Single Responsibility (SRP)
 */
public class UserSearchHandler {
    
    private final UserManagementService userManagementService;
    private final TableView<Usuario> tableView;
    private final Label totalLabel;

    public UserSearchHandler(UserManagementService userManagementService, TableView<Usuario> tableView, Label totalLabel) {
        this.userManagementService = userManagementService;
        this.tableView = tableView;
        this.totalLabel = totalLabel;
    }

    /**
     * Configura o listener de busca em tempo real
     */
    public void configurarBuscaAutomatica(TextField searchField) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            realizarBusca(newValue);
        });
    }

    /**
     * Realiza a busca de usuários baseado no termo informado
     */
    public void realizarBusca(String termo) {
        ObservableList<Usuario> resultados = userManagementService.buscarUsuarios(termo);
        tableView.setItems(resultados);
        atualizarContador(resultados.size());
    }

    /**
     * Carrega todos os usuários do serviço
     */
    public ObservableList<Usuario> carregarTodosUsuarios() {
        ObservableList<Usuario> usuarios = userManagementService.listarUsuarios();
        tableView.setItems(usuarios);
        atualizarContador(usuarios.size());
        return usuarios;
    }

    /**
     * Atualiza o contador de usuários exibidos
     */
    private void atualizarContador(int total) {
        totalLabel.setText(total + " usuário(s) encontrado(s)");
    }

    /**
     * Limpa a busca e recarrega todos os usuários
     */
    public void limparBusca(TextField searchField) {
        searchField.clear();
        carregarTodosUsuarios();
    }
}
