package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.suptec.controllers.helpers.ChamadoActionHandler;
import br.com.suptec.controllers.helpers.ChamadoSearchHandler;
import br.com.suptec.controllers.helpers.ChamadoSelectionManager;
import br.com.suptec.controllers.helpers.ChamadoTableConfigurator;
import br.com.suptec.core.SceneManager;
import br.com.suptec.models.Chamado;
import br.com.suptec.services.ChamadoService;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller para a tela de listagem de chamados
 * Seguindo princípios SOLID (SRP, OCP, DIP)
 */
public class ChamadoListController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filtroPrioridade;

    @FXML
    private TableView<Chamado> chamadoTable;

    @FXML
    private TableColumn<Chamado, String> colId;

    @FXML
    private TableColumn<Chamado, String> colTitulo;

    @FXML
    private TableColumn<Chamado, String> colPrioridade;

    @FXML
    private TableColumn<Chamado, String> colStatus;

    @FXML
    private TableColumn<Chamado, String> colNomeUsuario;

    @FXML
    private TableColumn<Chamado, String> colEmailUsuario;

    @FXML
    private TableColumn<Chamado, String> colSetorUsuario;

    @FXML
    private TableColumn<Chamado, String> colDataAbertura;

    @FXML
    private TableColumn<Chamado, String> colTecnicoResponsavel;

    @FXML
    private TableColumn<Chamado, String> colRespostaDoTecnico;

    @FXML
    private Label totalChamadosLabel;

    @FXML
    private Button btnVoltar;

    @FXML
    private Button btnInformacoes;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnAtualizar;

    private final ChamadoService chamadoService;
    private ChamadoTableConfigurator tableConfigurator;
    private ChamadoSearchHandler searchHandler;
    private ChamadoSelectionManager selectionManager;
    private ChamadoActionHandler actionHandler;

    public ChamadoListController() {
        this.chamadoService = ChamadoService.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarColaboradores();
        configurarComponentes();
    }

    private void inicializarColaboradores() {
        this.tableConfigurator = new ChamadoTableConfigurator(chamadoTable);
        this.searchHandler = new ChamadoSearchHandler(
            chamadoService, 
            chamadoTable, 
            totalChamadosLabel,
            filtroPrioridade
        );
        this.selectionManager = new ChamadoSelectionManager(
            chamadoTable, 
            btnInformacoes, 
            btnEditar, 
            btnExcluir
        );
        this.actionHandler = new ChamadoActionHandler(this::recarregarChamados);
    }

    private void configurarComponentes() {
        tableConfigurator.configurarColunas(
            colId,
            colTitulo,
            colPrioridade,
            colStatus,
            colNomeUsuario,
            colEmailUsuario,
            colSetorUsuario,
            colDataAbertura,
            colTecnicoResponsavel,
            colRespostaDoTecnico
        );
        
        searchHandler.inicializarFiltros();
        searchHandler.configurarBuscaAutomatica(searchField);
        searchHandler.configurarFiltroPrioridade(searchField);
        selectionManager.configurarSelecao();
        recarregarChamados();
    }

    private void recarregarChamados() {
        try {
            searchHandler.carregarTodosChamados();
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Nao foi possivel carregar a lista de chamados.");
            System.err.println("Erro ao carregar chamados: " + e.getMessage());
        }
    }

    @FXML
    private void handleVoltar() {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        SceneManager.replaceRootPreserveStage(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu Principal");
    }

    @FXML
    private void handleAtualizar() {
        recarregarChamados();
        AlertUtils.showInfo("Atualizado", "Lista de chamados atualizada com sucesso!");
    }

    @FXML
    private void handleVisualizarDetalhes() {
        Chamado chamado = selectionManager.getChamadoSelecionado();
        actionHandler.visualizarChamado(chamado);
    }

    @FXML
    private void handleEditar() {
        Chamado chamado = selectionManager.getChamadoSelecionado();
        actionHandler.editarChamado(chamado);
    }

    @FXML
    private void handleExcluir() {
        Chamado chamado = selectionManager.getChamadoSelecionado();
        actionHandler.excluirChamado(chamado);
    }
}
