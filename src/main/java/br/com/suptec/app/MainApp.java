package br.com.suptec.app;

import br.com.suptec.core.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.loadScene(stage, "/fxml/LoginView.fxml", "SUPTEC - Sistema De Chamados Técnicos");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
