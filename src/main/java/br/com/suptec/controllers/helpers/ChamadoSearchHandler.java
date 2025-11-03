package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Chamado;
import br.com.suptec.services.ChamadoService;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Classe responsável por gerenciar a busca e filtragem de chamados
 * Aplica o princípio Single Responsibility (SRP)
 */
public class ChamadoSearchHandler {
    
    private final ChamadoService chamadoService;
    private final TableView<Chamado> tableView;
    private final Label totalLabel;
    private final ComboBox<String> filtroPrioridade;

    public ChamadoSearchHandler(
            ChamadoService chamadoService, 
            TableView<Chamado> tableView, 
            Label totalLabel,
            ComboBox<String> filtroPrioridade) {
        
        this.chamadoService = chamadoService;
        this.tableView = tableView;
        this.totalLabel = totalLabel;
        this.filtroPrioridade = filtroPrioridade;
    }

    public void configurarBuscaAutomatica(TextField searchField) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            realizarBusca(newValue);
        });
    }

    public void realizarBusca(String termo) {
        ObservableList<Chamado> resultados = chamadoService.buscarChamados(termo);
        
        String prioridadeSelecionada = filtroPrioridade.getValue();
        if (prioridadeSelecionada != null && !prioridadeSelecionada.equals("Todas")) {
            resultados = filtrarPorPrioridade(resultados, prioridadeSelecionada);
        }
        
        tableView.setItems(resultados);
        atualizarContador(resultados.size());
    }

    private ObservableList<Chamado> filtrarPorPrioridade(ObservableList<Chamado> chamados, String prioridade) {
        return chamados.filtered(c -> 
            c.getPrioridade() != null && c.getPrioridade().equalsIgnoreCase(prioridade)
        );
    }

    public ObservableList<Chamado> carregarTodosChamados() {
        ObservableList<Chamado> chamados = chamadoService.listarChamados();
        tableView.setItems(chamados);
        atualizarContador(chamados.size());
        return chamados;
    }

    private void atualizarContador(int total) {
        totalLabel.setText(total + " chamado(s) encontrado(s)");
    }

    public void limparBusca(TextField searchField) {
        searchField.clear();
        limparFiltros();
        carregarTodosChamados();
    }

    public void limparFiltros() {
        filtroPrioridade.setValue("Todas");
    }

    public void inicializarFiltros() {
        filtroPrioridade.getItems().addAll("Todas", "Baixa", "Media", "Alta");
        filtroPrioridade.setValue("Todas");
    }

    public void configurarFiltroPrioridade(TextField searchField) {
        filtroPrioridade.valueProperty().addListener((obs, oldVal, newVal) -> {
            realizarBusca(searchField.getText());
        });
    }
}
