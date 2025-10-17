package br.com.suptec.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SceneManager {

    private static final String DEFAULT_STYLE = "/css/main.css";

    private SceneManager() {} // impede instanciação

    public static void loadScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(SceneManager.class.getResource(DEFAULT_STYLE).toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.getIcons().add(new Image("/images/LogoPrincipal.jpg"));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.err.println("[SceneManager] Falha ao carregar cena: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
