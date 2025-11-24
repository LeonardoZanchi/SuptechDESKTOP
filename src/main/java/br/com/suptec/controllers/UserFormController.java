package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.suptec.core.SceneManager;
import br.com.suptec.models.Usuario.TipoUsuario;
import br.com.suptec.services.UserRegistrationService;
import br.com.suptec.utils.AlertUtils;
import br.com.suptec.utils.FieldValidator;
import br.com.suptec.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller para a tela de cadastro/edição de usuários
 */
public class UserFormController implements Initializable {

    @FXML private ComboBox<TipoUsuario> cmbTipoUsuario;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtSetor;
    @FXML private TextField txtEspecialidade;
    @FXML private VBox vboxSetor;
    @FXML private VBox vboxEspecialidade;

    private final UserRegistrationService userRegistrationService;

    public UserFormController() {
        this.userRegistrationService = UserRegistrationService.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarComboTipoUsuario();
        configurarListenerTipoUsuario();
        configurarValidacoesCampos();
    }

    /**
     * Configura o ComboBox com os tipos de usuário
     */
    private void configurarComboTipoUsuario() {
        cmbTipoUsuario.getItems().addAll(
            TipoUsuario.GERENTE,
            TipoUsuario.TECNICO,
            TipoUsuario.USUARIO
        );
    }

    /**
     * Configura o listener para mostrar/ocultar campos baseado no tipo
     */
    private void configurarListenerTipoUsuario() {
        cmbTipoUsuario.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarCamposVisiveis(newVal);
            }
        });
    }

    /**
     * Configura validações em tempo real nos campos
     */
    private void configurarValidacoesCampos() {
        // Usar FieldValidator para configurar validações
        FieldValidator.configurarValidacaoNome(txtNome);
        FieldValidator.configurarValidacaoTelefone(txtTelefone);
        FieldValidator.configurarValidacaoEmail(txtEmail);
        FieldValidator.configurarValidacaoTextoApenasLetras(txtSetor);
        FieldValidator.configurarValidacaoTextoApenasLetras(txtEspecialidade);
    }

    /**
     * Atualiza a visibilidade dos campos baseado no tipo de usuário
     */
    private void atualizarCamposVisiveis(TipoUsuario tipo) {
        if (tipo == TipoUsuario.TECNICO) {
            // Técnico: mostrar especialidade, ocultar setor
            vboxEspecialidade.setVisible(true);
            vboxEspecialidade.setManaged(true);
            vboxSetor.setVisible(false);
            vboxSetor.setManaged(false);
        } else {
            // Gerente e Usuário: mostrar setor, ocultar especialidade
            vboxSetor.setVisible(true);
            vboxSetor.setManaged(true);
            vboxEspecialidade.setVisible(false);
            vboxEspecialidade.setManaged(false);
        }
    }

    /**
     * Handler do botão Salvar
     */
    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        TipoUsuario tipo = cmbTipoUsuario.getValue();
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();
        String telefone = txtTelefone.getText().trim();

        boolean sucesso = false;

        try {
            switch (tipo) {
                case GERENTE:
                    String setor = txtSetor.getText().trim();
                    sucesso = userRegistrationService.adicionarGerente(nome, email, senha, telefone, setor);
                    break;

                case TECNICO:
                    String especialidade = txtEspecialidade.getText().trim();
                    sucesso = userRegistrationService.adicionarTecnico(nome, email, senha, telefone, especialidade);
                    break;

                case USUARIO:
                    String setorUsuario = txtSetor.getText().trim();
                    sucesso = userRegistrationService.adicionarUsuario(nome, email, senha, telefone, setorUsuario);
                    break;
            }

            if (sucesso) {
                AlertUtils.showInfo("Sucesso", 
                    tipo.getDescricao() + " cadastrado com sucesso!\n\n" +
                    "Nome: " + nome + "\n" +
                    "Email: " + email);
                
                voltarParaListagem();
            } else {
                AlertUtils.showError("Erro ao cadastrar", 
                    "Não foi possível cadastrar o " + tipo.getDescricao().toLowerCase() + ".\n\n" +
                    "Verifique os dados e tente novamente.\n" +
                    "Se o erro persistir, verifique se o email já está cadastrado.");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao cadastrar usuário: " + e.getMessage());
            AlertUtils.showError("Erro inesperado", 
                "Ocorreu um erro ao tentar cadastrar o usuário:\n" + e.getMessage());
        }
    }

    /**
     * Handler do botão Cancelar
     */
    @FXML
    private void handleCancelar() {
        if (algumCampoPreenchido()) {
            boolean confirmar = AlertUtils.showConfirmation(
                "Cancelar cadastro", 
                "Existem dados não salvos.\n\nDeseja realmente cancelar?"
            );
            
            if (!confirmar) {
                return;
            }
        }
        
        voltarParaListagem();
    }

    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        // Validar tipo de usuário
        if (cmbTipoUsuario.getValue() == null) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, selecione o tipo de usuário.");
            cmbTipoUsuario.requestFocus();
            return false;
        }

        // Validar nome
        String nome = txtNome.getText().trim();
        if (!ValidationUtils.validarCampoObrigatorio(nome)) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha o nome completo.");
            txtNome.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarNome(nome)) {
            AlertUtils.showWarning("Nome inválido", 
                "O nome deve conter apenas letras e ter no mínimo 3 caracteres.\n" +
                "Números não são permitidos.");
            txtNome.requestFocus();
            return false;
        }

        // Validar email
        String email = txtEmail.getText().trim();
        if (!ValidationUtils.validarCampoObrigatorio(email)) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha o email.");
            txtEmail.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarEmail(email)) {
            AlertUtils.showWarning("Email inválido", 
                "Por favor, digite um email válido.\n" +
                "O email deve conter @ e um domínio válido (ex: .com, .com.br).");
            txtEmail.requestFocus();
            return false;
        }

        // Validar senha
        String senha = txtSenha.getText().trim();
        if (!ValidationUtils.validarCampoObrigatorio(senha)) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha a senha.");
            txtSenha.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarSenha(senha, 6)) {
            AlertUtils.showWarning("Senha inválida", "A senha deve ter no mínimo 6 caracteres.");
            txtSenha.requestFocus();
            return false;
        }

        // Validar telefone
        String telefone = txtTelefone.getText().trim();
        if (!ValidationUtils.validarCampoObrigatorio(telefone)) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha o telefone.");
            txtTelefone.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarTelefone(telefone)) {
            AlertUtils.showWarning("Telefone inválido", 
                "O telefone deve conter apenas números.\n" +
                "Formatos aceitos:\n" +
                "- Fixo sem DDD: 3272-2864 (8 dígitos)\n" +
                "- Celular sem DDD: 99999-9999 (9 dígitos)\n" +
                "- Fixo com DDD: (11) 3272-2864 (10 dígitos)\n" +
                "- Celular com DDD: (11) 99999-9999 (11 dígitos)");
            txtTelefone.requestFocus();
            return false;
        }

        // Validar setor ou especialidade baseado no tipo
        TipoUsuario tipo = cmbTipoUsuario.getValue();
        if (tipo == TipoUsuario.TECNICO) {
            String especialidade = txtEspecialidade.getText().trim();
            if (!ValidationUtils.validarCampoObrigatorio(especialidade)) {
                AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha a especialidade.");
                txtEspecialidade.requestFocus();
                return false;
            }
            if (!ValidationUtils.validarTextoApenasLetras(especialidade)) {
                AlertUtils.showWarning("Especialidade inválida", 
                    "A especialidade deve conter apenas letras.\n" +
                    "Números não são permitidos.");
                txtEspecialidade.requestFocus();
                return false;
            }
        } else {
            String setor = txtSetor.getText().trim();
            if (!ValidationUtils.validarCampoObrigatorio(setor)) {
                AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha o setor.");
                txtSetor.requestFocus();
                return false;
            }
            if (!ValidationUtils.validarTextoApenasLetras(setor)) {
                AlertUtils.showWarning("Setor inválido", 
                    "O setor deve conter apenas letras.\n" +
                    "Números não são permitidos.");
                txtSetor.requestFocus();
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica se algum campo foi preenchido
     */
    private boolean algumCampoPreenchido() {
        return cmbTipoUsuario.getValue() != null ||
               !txtNome.getText().trim().isEmpty() ||
               !txtEmail.getText().trim().isEmpty() ||
               !txtSenha.getText().trim().isEmpty() ||
               !txtTelefone.getText().trim().isEmpty() ||
               !txtSetor.getText().trim().isEmpty() ||
               !txtEspecialidade.getText().trim().isEmpty();
    }

    /**
     * Volta para a tela de listagem de usuários
     */
    private void voltarParaListagem() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        SceneManager.replaceRootPreserveStage(stage, "/fxml/UserListView.fxml", "SUPTEC - Gerenciar Usuários");
    }
}
