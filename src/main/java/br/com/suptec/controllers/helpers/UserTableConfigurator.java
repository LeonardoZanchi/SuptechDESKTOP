package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Usuario;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe responsável pela configuração da TableView de usuários
 * Aplica o princípio Single Responsibility (SRP)
 */
public class UserTableConfigurator {
    
    private final TableView<Usuario> tableView;

    public UserTableConfigurator(TableView<Usuario> tableView) {
        this.tableView = tableView;
    }

    /**
     * Configura todas as colunas da tabela
     */
    public void configurarColunas(
            TableColumn<Usuario, String> colNome,
            TableColumn<Usuario, String> colEmail,
            TableColumn<Usuario, String> colTelefone,
            TableColumn<Usuario, String> colSetor,
            TableColumn<Usuario, String> colTipo) {
        
        // Vincular colunas às propriedades do modelo
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colSetor.setCellValueFactory(new PropertyValueFactory<>("setorOuEspecialidade")); // Exibe Setor ou Especialidade
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDescricao"));

        // Configurar largura proporcional
        configurarLarguraColunas(colNome, colEmail, colTelefone, colSetor, colTipo);
    }

    /**
     * Configura a largura proporcional das colunas
     */
    private void configurarLarguraColunas(
            TableColumn<Usuario, String> colNome,
            TableColumn<Usuario, String> colEmail,
            TableColumn<Usuario, String> colTelefone,
            TableColumn<Usuario, String> colSetor,
            TableColumn<Usuario, String> colTipo) {
        
        colNome.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        colEmail.prefWidthProperty().bind(tableView.widthProperty().multiply(0.30));
        colTelefone.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        colSetor.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        colTipo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
    }
}
