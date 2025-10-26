package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.suptec.controllers.helpers.UserActionHandler;
import br.com.suptec.controllers.helpers.UserSearchHandler;
import br.com.suptec.controllers.helpers.UserSelectionManager;
import br.com.suptec.controllers.helpers.UserTableConfigurator;
import br.com.suptec.core.SceneManager;
import br.com.suptec.models.Usuario;
import br.com.suptec.services.UserManagementService;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para a tela de listagem de usuários
 * Refatorado seguindo princípios SOLID:
 * - SRP: Cada classe auxiliar tem uma única responsabilidade
 * - OCP: Aberto para extensão, fechado para modificação
 * - DIP: Depende de abstrações (interfaces implícitas via colaboradores)
 * 
 * Responsabilidades do Controller:
 * - Orquestrar as classes auxiliares (Coordenação)
 * - Gerenciar eventos dos componentes FXML
 * - Navegação entre telas
 */
public class UserListController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Usuario> userTable;

    @FXML
    private TableColumn<Usuario, String> colNome;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colTelefone;

    @FXML
    private TableColumn<Usuario, String> colSetor;

    @FXML
    private TableColumn<Usuario, String> colTipo;

    @FXML
    private Label totalUsuariosLabel;

    @FXML
    private Button btnVoltar;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnInformacoes;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    // Colaboradores - Classes auxiliares (Composição ao invés de herança)
    private final UserManagementService userManagementService;
    private UserTableConfigurator tableConfigurator;
    private UserSearchHandler searchHandler;
    private UserSelectionManager selectionManager;
    private UserActionHandler actionHandler;

    public UserListController() {
        // Usar a instância singleton do serviço de gerenciamento de usuários
        this.userManagementService = UserManagementService.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarColaboradores();
        configurarComponentes();
    }

    /**
     * Inicializa as classes auxiliares (colaboradores)
     * Aplica Injeção de Dependências manual
     */
    private void inicializarColaboradores() {
        this.tableConfigurator = new UserTableConfigurator(userTable);
        this.searchHandler = new UserSearchHandler(userManagementService, userTable, totalUsuariosLabel);
        this.selectionManager = new UserSelectionManager(userTable, btnInformacoes, btnEditar, btnExcluir);
        this.actionHandler = new UserActionHandler(this::recarregarUsuarios);
    }

    /**
     * Configura todos os componentes da tela
     */
    private void configurarComponentes() {
        tableConfigurator.configurarColunas(colNome, colEmail, colTelefone, colSetor, colTipo);
        searchHandler.configurarBuscaAutomatica(searchField);
        selectionManager.configurarSelecao();
        recarregarUsuarios();
    }

    /**
     * Recarrega a lista de usuários (usado como callback)
     */
    private void recarregarUsuarios() {
        try {
            searchHandler.carregarTodosUsuarios();
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar a lista de usuários.");
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    // ==================== HANDLERS DE NAVEGAÇÃO ====================

    /**
     * Retorna ao menu principal
     */
    @FXML
    private void handleVoltar() {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        SceneManager.loadScene(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu Principal", 1600, 1000, true, false);
    }

    // ==================== HANDLERS DE AÇÕES DE USUÁRIOS ====================

    /**
     * Abre tela para cadastrar novo usuário
     */
    @FXML
    private void handleNovo() {
        Stage stage = (Stage) btnNovo.getScene().getWindow();
        SceneManager.loadScene(stage, "/fxml/UserFormView.fxml", "SUPTEC - Novo Usuário", 1600, 1000, true, false);
    }

    /**
     * Abre tela para editar usuário selecionado
     */
    @FXML
    private void handleEditar() {
        Usuario selecionado = selectionManager.getUsuarioSelecionado();
        actionHandler.handleEditar(selecionado);
    }

    /**
     * Exclui o usuário selecionado após confirmação
     */
    @FXML
    private void handleExcluir() {
        Usuario selecionado = selectionManager.getUsuarioSelecionado();
        actionHandler.handleExcluir(selecionado);
    }

    /**
     * Visualiza detalhes do usuário selecionado
     */
    @FXML
    private void handleVisualizarDetalhes() {
        Usuario selecionado = selectionManager.getUsuarioSelecionado();
        actionHandler.handleVisualizarDetalhes(selecionado);
    }

    /**
     * Atualiza a lista de usuários
     */
    @FXML
    private void handleAtualizar() {
        searchHandler.limparBusca(searchField);
        AlertUtils.showSuccess("Atualizado", "Lista de usuários atualizada!");
    }
}
