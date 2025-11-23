package br.com.suptec.controllers.helpers;

import br.com.suptec.models.Chamado;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe responsável pela configuração da TableView de chamados
 * Aplica o princípio Single Responsibility (SRP)
 */
public class ChamadoTableConfigurator {
    
    private final TableView<Chamado> tableView;

    public ChamadoTableConfigurator(TableView<Chamado> tableView) {
        this.tableView = tableView;
    }

    /**
     * Configura todas as colunas da tabela
     */
    public void configurarColunas(
        TableColumn<Chamado, String> colId,
        TableColumn<Chamado, String> colTitulo,
        TableColumn<Chamado, String> colPrioridade,
        TableColumn<Chamado, String> colStatus,
        TableColumn<Chamado, String> colNomeUsuario,
        TableColumn<Chamado, String> colEmailUsuario,
        TableColumn<Chamado, String> colSetorUsuario,
        TableColumn<Chamado, String> colDataAbertura,
        TableColumn<Chamado, String> colRespostaDoTecnico) {
        
        colId.setCellValueFactory(new PropertyValueFactory<>("chamadoID"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPrioridade.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
    colNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeDoUsuario"));
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("emailFormatado"));
        colSetorUsuario.setCellValueFactory(new PropertyValueFactory<>("setorFormatado"));
        colDataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAberturaFormatada"));
        colRespostaDoTecnico.setCellValueFactory(new PropertyValueFactory<>("respostaDoTecnicoFormatada"));

    aplicarEstilosPrioridade(colPrioridade);
    configurarLarguraColunas(colId, colTitulo, colPrioridade, colStatus, colNomeUsuario, 
                colEmailUsuario, colSetorUsuario, colDataAbertura, colRespostaDoTecnico);
    }

    private void aplicarEstilosPrioridade(TableColumn<Chamado, String> colPrioridade) {
        colPrioridade.setCellFactory(column -> new TableCell<Chamado, String>() {
            @Override
            protected void updateItem(String prioridade, boolean empty) {
                super.updateItem(prioridade, empty);
                
                if (empty || prioridade == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(prioridade);
                    
                    switch (prioridade.toLowerCase()) {
                        case "baixa":
                            setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                            break;
                        case "media":
                        case "média":
                            setStyle("-fx-text-fill: #ffc107; -fx-font-weight: bold;");
                            break;
                        case "alta":
                            setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    private void configurarLarguraColunas(
            TableColumn<Chamado, String> colId,
            TableColumn<Chamado, String> colTitulo,
            TableColumn<Chamado, String> colPrioridade,
            TableColumn<Chamado, String> colStatus,
            TableColumn<Chamado, String> colNomeUsuario,
            TableColumn<Chamado, String> colEmailUsuario,
            TableColumn<Chamado, String> colSetorUsuario,
            TableColumn<Chamado, String> colDataAbertura,
            TableColumn<Chamado, String> colRespostaDoTecnico) {
        
        // Novas proporções para acomodar a coluna Status e Resposta do Técnico
        colId.prefWidthProperty().bind(tableView.widthProperty().multiply(0.05));
        colTitulo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.18));
        colPrioridade.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        colStatus.prefWidthProperty().bind(tableView.widthProperty().multiply(0.07));
        colNomeUsuario.prefWidthProperty().bind(tableView.widthProperty().multiply(0.13));
        colEmailUsuario.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        colSetorUsuario.prefWidthProperty().bind(tableView.widthProperty().multiply(0.09));
        colDataAbertura.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        colRespostaDoTecnico.prefWidthProperty().bind(tableView.widthProperty().multiply(0.17));
    }
}
