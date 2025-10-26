package br.com.suptec.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SceneManager {

    private static final String DEFAULT_STYLE = "/css/main.css";

    private SceneManager() {} // impede instanciação

    public static void loadScene(Stage stage, String fxmlPath, String title) {
        loadScene(stage, fxmlPath, title, 0, 0, false, true);
    }

    public static void loadScene(Stage stage, String fxmlPath, String title, double width, double height, boolean resizable) {
        loadScene(stage, fxmlPath, title, width, height, resizable, true);
    }

    public static void loadScene(Stage stage, String fxmlPath, String title, double width, double height, boolean resizable, boolean maximizable) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene scene;
            
            if (width > 0 && height > 0) {
                scene = new Scene(loader.load(), width, height);
            } else {
                scene = new Scene(loader.load());
            }
            
            scene.getStylesheets().add(SceneManager.class.getResource(DEFAULT_STYLE).toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.getIcons().add(new Image("/images/LogoPrincipal.jpg"));
            stage.setResizable(resizable);
            stage.setMaximized(false);
            
            // Desabilitar o botão de maximizar se necessário
            if (!maximizable) {
                stage.setResizable(false);
            }
            
            if (width > 0 && height > 0) {
                stage.centerOnScreen();
            }
            
            stage.show();
        } catch (Exception e) {
            System.err.println("✗ [SceneManager] Falha ao carregar cena: " + fxmlPath + " - " + e.getMessage());
        }
    }
}
