package br.com.suptec.controllers;

import br.com.suptec.models.Chamado;
import br.com.suptec.models.Usuario;
import br.com.suptec.services.ChamadoService;
import br.com.suptec.services.UserManagementService;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller para a tela de edição de chamados
 */
public class ChamadoEditController {

    @FXML private Label lblSubtitulo;

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> cmbPrioridade;
    @FXML private Label lblStatus;
    @FXML private ComboBox<String> cmbTecnicoResponsavel;
    @FXML private Label lblDataAbertura;

    @FXML private Label lblNomeUsuario;
    @FXML private Label lblEmailUsuario;
    @FXML private Label lblSetorUsuario;

    @FXML private TextArea txtDescricao;
    @FXML private TextArea txtRespostaDoTecnico;

    private Chamado chamadoOriginal;
    private Runnable onSaveCallback;
    private final ChamadoService chamadoService;

    public ChamadoEditController() {
        this.chamadoService = ChamadoService.getInstance();
    }

    @FXML
    public void initialize() {
        // Prioridades padr\u00e3o
        cmbPrioridade.getItems().addAll("Baixa", "Media", "Alta");

        // Carregar lista de t\u00e9cnicos
        carregarTecnicos();
    }

    public void setChamado(Chamado chamado) {
        if (chamado == null) {
            AlertUtils.showError("Erro", "Chamado inválido para edição");
            fecharJanela();
            return;
        }

        this.chamadoOriginal = chamado;
        preencherCampos();
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private void preencherCampos() {
        lblSubtitulo.setText("Editando: " + chamadoOriginal.getChamadoID());

        txtTitulo.setText(chamadoOriginal.getTitulo());
        txtDescricao.setText(chamadoOriginal.getDescricao());

        if (chamadoOriginal.getPrioridade() != null) {
            cmbPrioridade.setValue(chamadoOriginal.getPrioridade());
        }

        // Status nao editavel - apenas exibir
        if (chamadoOriginal.getStatus() != null) {
            lblStatus.setText(chamadoOriginal.getStatus());
        }

        // Tecnico responsavel - NAO autopreencher, deixar vazio para seleção manual
        // Removido o autopreenchimento para permitir seleção limpa

        lblDataAbertura.setText(chamadoOriginal.getDataAberturaFormatada());
        lblNomeUsuario.setText(chamadoOriginal.getNomeDoUsuario());
        lblEmailUsuario.setText(chamadoOriginal.getEmailFormatado());
        lblSetorUsuario.setText(chamadoOriginal.getSetorFormatado());
        
        // Resposta do técnico (pode ser null)
        if (chamadoOriginal.getRespostaDoTecnico() != null && !chamadoOriginal.getRespostaDoTecnico().trim().isEmpty()) {
            txtRespostaDoTecnico.setText(chamadoOriginal.getRespostaDoTecnico());
        } else {
            txtRespostaDoTecnico.setText("Sem resposta do técnico ainda.");
            txtRespostaDoTecnico.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
        }
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) return;

        String tecnicoNome = cmbTecnicoResponsavel.getValue() != null ? cmbTecnicoResponsavel.getValue() : "Nenhum";
        
        boolean confirmado = AlertUtils.showConfirmation(
            "Confirmar Edição",
            String.format("Deseja salvar as alterações do chamado?\n\nTitulo: %s\nPrioridade: %s\nTécnico: %s",
                txtTitulo.getText(), cmbPrioridade.getValue(), tecnicoNome)
        );

        if (!confirmado) return;

        Chamado atualizado = construirChamadoAtualizado();

        boolean sucesso = chamadoService.atualizarChamado(atualizado);

        if (sucesso) {
            AlertUtils.showSuccess("Sucesso", "Chamado atualizado com sucesso!");
            if (onSaveCallback != null) onSaveCallback.run();
            fecharJanela();
        } else {
            AlertUtils.showError("Erro ao Atualizar", "Não foi possível atualizar o chamado. Verifique logs e API.");
        }
    }

    @FXML
    private void handleCancelar() {
        boolean confirmado = AlertUtils.showConfirmation("Cancelar Edição", "Deseja realmente cancelar? Todas as alterações serão perdidas.");
        if (confirmado) fecharJanela();
    }

    private boolean validarCampos() {
        if (txtTitulo.getText() == null || txtTitulo.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha o título do chamado.");
            txtTitulo.requestFocus();
            return false;
        }

        if (cmbPrioridade.getValue() == null || cmbPrioridade.getValue().trim().isEmpty()) {
            AlertUtils.showWarning("Campo Obrigat\u00f3rio", "Por favor, selecione a prioridade.");
            cmbPrioridade.requestFocus();
            return false;
        }

        return true;
    }

    private Chamado construirChamadoAtualizado() {
        Chamado c = new Chamado();
        c.setChamadoID(chamadoOriginal.getChamadoID());
        c.setTitulo(txtTitulo.getText().trim());
        c.setDescricao(txtDescricao.getText() != null ? txtDescricao.getText().trim() : null);
        c.setPrioridade(cmbPrioridade.getValue());
        c.setStatus(chamadoOriginal.getStatus()); // Mant\u00e9m o status original
        
        // T\u00e9cnico respons\u00e1vel
        if (cmbTecnicoResponsavel.getValue() != null && !cmbTecnicoResponsavel.getValue().trim().isEmpty()) {
            c.setTecnicoResponsavel(cmbTecnicoResponsavel.getValue());
        }
        
        // Resposta do t\u00e9cnico (pode ser editada)
        String respostaTecnico = txtRespostaDoTecnico.getText();
        if (respostaTecnico != null && !respostaTecnico.trim().isEmpty() 
            && !respostaTecnico.equals("Sem resposta do técnico ainda.")) {
            c.setRespostaDoTecnico(respostaTecnico.trim());
        } else {
            c.setRespostaDoTecnico(null);
        }
        
        // manter usuário/setor não editáveis aqui
        c.setNomeDoUsuario(chamadoOriginal.getNomeDoUsuario());
        c.setEmailDoUsuario(chamadoOriginal.getEmailDoUsuario());
        c.setSetorDoUsuario(chamadoOriginal.getSetorDoUsuario());
        c.setDataAbertura(chamadoOriginal.getDataAbertura());
        return c;
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }

    private void carregarTecnicos() {
        try {
            UserManagementService userService = UserManagementService.getInstance();
            List<Usuario> tecnicos = userService.listarUsuarios().stream()
                .filter(u -> u.getTipo() == Usuario.TipoUsuario.TECNICO)
                .toList();
            
            cmbTecnicoResponsavel.getItems().clear();
            cmbTecnicoResponsavel.getItems().add("Nenhum"); // Opção para remover técnico
            
            for (Usuario tecnico : tecnicos) {
                cmbTecnicoResponsavel.getItems().add(tecnico.getEmail());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar técnicos: " + e.getMessage());
            AlertUtils.showWarning("Aviso", "Não foi possível carregar a lista de técnicos.");
        }
    }
}
