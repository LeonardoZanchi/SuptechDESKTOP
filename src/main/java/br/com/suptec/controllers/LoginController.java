package br.com.suptec.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import br.com.suptec.core.SceneManager;
import br.com.suptec.services.UsuarioService;
import br.com.suptec.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private CheckBox chkLembrar;
    @FXML private ImageView imgLogo;

    private final UsuarioService usuarioService = new UsuarioService();
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
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();

        boolean valido = usuarioService.validarLogin(usuario, senha);
        if (valido) {
            // Salvar preferência de lembrar usuário
            prefs.putBoolean("lembrar_usuario", chkLembrar.isSelected());
            if (chkLembrar.isSelected()) {
                prefs.put("usuario", usuario);
            } else {
                prefs.remove("usuario");
            }

            // Navegar para o menu principal
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            SceneManager.loadScene(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu Principal");
        } else {
            AlertUtils.showError("Credenciais inválidas", "Usuário ou senha incorretos.");
        }
    }
}
