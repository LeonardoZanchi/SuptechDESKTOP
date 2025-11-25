package br.com.suptec.controllers;

import br.com.suptec.models.Usuario;
import br.com.suptec.models.Usuario.TipoUsuario;
import br.com.suptec.services.UserManagementService;
import br.com.suptec.utils.AlertUtils;
import br.com.suptec.utils.FieldValidator;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller para a tela de edição de usuários
 */
public class UserEditController {

    @FXML private Label lblSubtitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private Label lblTipo;
    @FXML private VBox vboxSetor;
    @FXML private TextField txtSetor;
    @FXML private VBox vboxEspecialidade;
    @FXML private TextField txtEspecialidade;
    @FXML private CheckBox chkAlterarSenha;
    @FXML private VBox vboxNovaSenha;
    @FXML private PasswordField txtNovaSenha;
    @FXML private VBox vboxConfirmarSenha;
    @FXML private PasswordField txtConfirmarSenha;

    private Usuario usuarioOriginal;
    private Runnable onSaveCallback;
    private final UserManagementService userManagementService;

    public UserEditController() {
        this.userManagementService = UserManagementService.getInstance();
    }

    @FXML
    public void initialize() {
        // Configurar validações de campo em tempo real
        configurarValidacoesCampos();

        // Listener para mostrar/ocultar campos de senha
        chkAlterarSenha.selectedProperty().addListener((obs, oldVal, newVal) -> {
            vboxNovaSenha.setVisible(newVal);
            vboxNovaSenha.setManaged(newVal);
            vboxConfirmarSenha.setVisible(newVal);
            vboxConfirmarSenha.setManaged(newVal);
            
            // Limpar campos de senha quando desmarcar
            if (!newVal) {
                txtNovaSenha.clear();
                txtConfirmarSenha.clear();
            }
        });
    }

    /**
     * Configura validações em tempo real para todos os campos do formulário
     */
    private void configurarValidacoesCampos() {
        // Nome: apenas letras e espaços (sem números)
        FieldValidator.configurarValidacaoNome(txtNome);
        
        // Email: caracteres válidos para email
        FieldValidator.configurarValidacaoEmail(txtEmail);
        
        // Telefone: apenas números, parênteses, hífen e espaço
        FieldValidator.configurarValidacaoTelefone(txtTelefone);
        
        // Setor: apenas letras e espaços (sem números)
        FieldValidator.configurarValidacaoTextoApenasLetras(txtSetor);
        
        // Especialidade: apenas letras e espaços (sem números)
        FieldValidator.configurarValidacaoTextoApenasLetras(txtEspecialidade);
    }

    /**
     * Configura o usuário a ser editado
     */
    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            AlertUtils.showError("Erro", "Usuário inválido para edição");
            fecharJanela();
            return;
        }

        this.usuarioOriginal = usuario;
        preencherCampos();
    }

    /**
     * Define o callback a ser executado após salvar
     */
    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    /**
     * Preenche os campos com os dados do usuário
     */
    private void preencherCampos() {
        lblSubtitulo.setText("Editando: " + usuarioOriginal.getNome());
        txtNome.setText(usuarioOriginal.getNome());
        txtEmail.setText(usuarioOriginal.getEmail());
        txtTelefone.setText(usuarioOriginal.getTelefone());
        lblTipo.setText(usuarioOriginal.getTipo().getDescricao());
        
        // Atualizar campos visíveis baseado no tipo original
        atualizarCamposVisiveis(usuarioOriginal.getTipo());
        
        if (usuarioOriginal.getSetor() != null && !usuarioOriginal.getSetor().equals("N/A")) {
            txtSetor.setText(usuarioOriginal.getSetor());
        }
        
        if (usuarioOriginal.getEspecialidade() != null && !usuarioOriginal.getEspecialidade().equals("N/A")) {
            txtEspecialidade.setText(usuarioOriginal.getEspecialidade());
        }
    }

    /**
     * Atualiza visibilidade dos campos baseado no tipo de usuário
     */
    private void atualizarCamposVisiveis(TipoUsuario tipo) {
        if (tipo == null) {
            vboxSetor.setVisible(false);
            vboxSetor.setManaged(false);
            vboxEspecialidade.setVisible(false);
            vboxEspecialidade.setManaged(false);
            return;
        }

        switch (tipo) {
            case GERENTE:
            case USUARIO:
                vboxSetor.setVisible(true);
                vboxSetor.setManaged(true);
                vboxEspecialidade.setVisible(false);
                vboxEspecialidade.setManaged(false);
                break;
            case TECNICO:
                vboxSetor.setVisible(false);
                vboxSetor.setManaged(false);
                vboxEspecialidade.setVisible(true);
                vboxEspecialidade.setManaged(true);
                break;
        }
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        // Confirmar edição
        boolean confirmado = AlertUtils.showConfirmation(
            "Confirmar Edição",
            String.format(
                "Deseja realmente salvar as alterações do usuário?\n\n" +
                "Nome: %s\n" +
                "Email: %s\n" +
                "Tipo: %s",
                txtNome.getText(),
                txtEmail.getText(),
                usuarioOriginal.getTipo().getDescricao()
            )
        );

        if (!confirmado) {
            return;
        }

        // Criar objeto com dados atualizados
        Usuario usuarioAtualizado = construirUsuarioAtualizado();

        // Enviar para API
        boolean sucesso = userManagementService.atualizarUsuario(usuarioAtualizado);

        if (sucesso) {
            AlertUtils.showSuccess(
                "Sucesso",
                "Usuário atualizado com sucesso!"
            );

            // Executar callback para recarregar lista
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            fecharJanela();
        } else {
            AlertUtils.showError(
                "Erro ao Atualizar",
                "Não foi possível atualizar o usuário.\n\n" +
                "Verifique:\n" +
                "• Conexão com a API\n" +
                "• Se o usuário ainda existe\n" +
                "• Logs do console para mais detalhes"
            );
        }
    }

    @FXML
    private void handleCancelar() {
        boolean confirmado = AlertUtils.showConfirmation(
            "Cancelar Edição",
            "Deseja realmente cancelar?\n\nTodas as alterações serão perdidas."
        );

        if (confirmado) {
            fecharJanela();
        }
    }

    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        // Nome
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha o nome do usuário.");
            txtNome.requestFocus();
            return false;
        }

        // Email
        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha o email do usuário.");
            txtEmail.requestFocus();
            return false;
        }

        if (!txtEmail.getText().contains("@")) {
            AlertUtils.showWarning("Email Inválido", "Por favor, insira um email válido.");
            txtEmail.requestFocus();
            return false;
        }

        // Telefone
        if (txtTelefone.getText() == null || txtTelefone.getText().trim().isEmpty()) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha o telefone do usuário.");
            txtTelefone.requestFocus();
            return false;
        }

        // Setor (para Gerentes e Usuários)
        if ((usuarioOriginal.getTipo() == TipoUsuario.GERENTE || usuarioOriginal.getTipo() == TipoUsuario.USUARIO) &&
            (txtSetor.getText() == null || txtSetor.getText().trim().isEmpty())) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha o setor do usuário.");
            txtSetor.requestFocus();
            return false;
        }

        // Especialidade (para Técnicos)
        if (usuarioOriginal.getTipo() == TipoUsuario.TECNICO &&
            (txtEspecialidade.getText() == null || txtEspecialidade.getText().trim().isEmpty())) {
            AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha a especialidade do técnico.");
            txtEspecialidade.requestFocus();
            return false;
        }

        // Validar senhas se checkbox marcado
        if (chkAlterarSenha.isSelected()) {
            if (txtNovaSenha.getText() == null || txtNovaSenha.getText().trim().isEmpty()) {
                AlertUtils.showWarning("Campo Obrigatório", "Por favor, preencha a nova senha.");
                txtNovaSenha.requestFocus();
                return false;
            }

            if (txtNovaSenha.getText().length() < 6) {
                AlertUtils.showWarning("Senha Fraca", "A senha deve ter no mínimo 6 caracteres.");
                txtNovaSenha.requestFocus();
                return false;
            }

            if (!txtNovaSenha.getText().equals(txtConfirmarSenha.getText())) {
                AlertUtils.showWarning("Senhas Diferentes", "A nova senha e a confirmação não coincidem.");
                txtConfirmarSenha.requestFocus();
                return false;
            }
        }

        return true;
    }

    /**
     * Constrói objeto Usuario com os dados atualizados
     */
    private Usuario construirUsuarioAtualizado() {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioOriginal.getId()); // Manter o ID original
        usuario.setNome(txtNome.getText().trim());
        usuario.setEmail(txtEmail.getText().trim());
        usuario.setTelefone(txtTelefone.getText().trim());
        usuario.setTipo(usuarioOriginal.getTipo()); // Manter tipo original (não editável)

        // Debug logs
        System.out.println("=== CONSTRUINDO USUÁRIO ATUALIZADO ===");
        System.out.println("ID: " + usuario.getId());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Telefone: " + usuario.getTelefone());
        System.out.println("Tipo: " + usuario.getTipo());

        // Senha (somente se alterando)
        if (chkAlterarSenha.isSelected()) {
            usuario.setSenha(txtNovaSenha.getText());
            System.out.println("Senha: SERÁ ALTERADA");
        } else {
            System.out.println("Senha: NÃO SERÁ ALTERADA");
        }

        // Setor ou Especialidade baseado no tipo
        if (usuarioOriginal.getTipo() == TipoUsuario.GERENTE || usuarioOriginal.getTipo() == TipoUsuario.USUARIO) {
            usuario.setSetor(txtSetor.getText().trim());
            System.out.println("Setor: " + usuario.getSetor());
        } else if (usuarioOriginal.getTipo() == TipoUsuario.TECNICO) {
            usuario.setEspecialidade(txtEspecialidade.getText().trim());
            System.out.println("Especialidade: " + usuario.getEspecialidade());
        }
        System.out.println("======================================");

        return usuario;
    }

    /**
     * Fecha a janela atual
     */
    private void fecharJanela() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}
