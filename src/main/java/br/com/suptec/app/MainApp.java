package br.com.suptec.app;

import br.com.suptec.core.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Ponto de entrada da aplicação SUPTEC Desktop.
 * Inicializa a aplicação JavaFX e carrega a tela de login.
 */
public class MainApp extends Application {

    /**
     * Método chamado pelo framework JavaFX ao iniciar a aplicação.
     * Carrega a cena inicial (LoginView.fxml) através do SceneManager.
     *
     * @param stage Stage principal da aplicação
     */
    @Override
    public void start(Stage stage) {
        SceneManager.loadScene(stage, "/fxml/LoginView.fxml", "SUPTEC - Sistema De Chamados Técnicos");
    }

    /**
     * Método main para execução a partir da linha de comando.
     * @param args argumentos de inicialização
     */
    public static void main(String[] args) {
        launch(args);
    }
}
