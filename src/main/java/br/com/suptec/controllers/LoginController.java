package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import br.com.suptec.core.SceneManager;
import br.com.suptec.services.AuthService;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Controller da tela de login.
 * Responsável por validar credenciais, controlar preferência "lembrar usuário"
 * e redirecionar para o menu principal quando o login for bem-sucedido.
 */
public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private CheckBox chkLembrar;
    @FXML private ImageView imgLogo;

    private final AuthService authService = AuthService.getInstance();
    private final Preferences prefs = Preferences.userNodeForPackage(LoginController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carregar a imagem da logo programaticamente
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/LogoSuptechLogin.jpg"));
            imgLogo.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Erro ao carregar logo: " + e.getMessage());
            // A imagem não será exibida, mas o programa continua funcionando
        }

        // Carregar preferência de lembrar usuário
        boolean lembrar = prefs.getBoolean("lembrar_usuario", false);
        chkLembrar.setSelected(lembrar);
        if (lembrar) {
            String usuarioSalvo = prefs.get("usuario", "");
            txtUsuario.setText(usuarioSalvo);
        }
    }

    @FXML
    private void onLogin() {
        String email = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();

        // Validar campos obrigatórios
        if (email.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Campos obrigatórios", "Por favor, preencha email e senha.");
            return;
        }

        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            AlertUtils.showWarning("Email inválido", "Por favor, digite um email válido.");
            return;
        }

        // Validar login através da API (apenas gerentes)
        boolean valido = authService.validarLogin(email, senha);
        if (valido) {
            // Salvar preferência de lembrar usuário
            prefs.putBoolean("lembrar_usuario", chkLembrar.isSelected());
            if (chkLembrar.isSelected()) {
                prefs.put("usuario", email);
            } else {
                prefs.remove("usuario");
            }

            // Obter nome do usuário
            String nomeUsuario = authService.getNomeUsuarioLogado();

            // Exibir mensagem de boas-vindas para gerente
            AlertUtils.showInfo("Acesso Autorizado!", 
                "Bem-vindo ao SUPTEC Desktop, " + nomeUsuario + "!\n\n" +
                "Acesso de gerente confirmado.\n" +
                "Você será redirecionado para o painel administrativo.");

            // Navegar para o menu principal com dimensões ampliadas e sem permitir maximizar
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            SceneManager.loadScene(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu Principal", 1600, 1000, true, false);
        } else {
            AlertUtils.showError("Acesso Negado", 
                "Acesso restrito apenas para gerentes.\n\n" +
                "Usuários técnicos e comuns devem usar o aplicativo mobile ou web.");
        }
    }
}
