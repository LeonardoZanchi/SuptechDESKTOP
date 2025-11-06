package br.com.suptec.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Utilitário responsável por carregar e gerenciar cenas (views) da aplicação.
 * Fornece métodos para carregar novas Scenes e também para substituir o root da
 * Scene atual preservando o tamanho e propriedades do Stage.
 */
public class SceneManager {

    private static final String DEFAULT_STYLE = "/css/main.css";

    // Construtor privado para evitar instanciação
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

    /**
     * Substitui o root (nó raiz) da Scene existente no Stage pelo root carregado do FXML.
     * Isso preserva o tamanho atual da Scene e as propriedades do Stage (evita redimensionamento inesperado).
     */
    public static void replaceRootPreserveStage(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            javafx.scene.Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                // ensure stylesheet is present
                String css = SceneManager.class.getResource(DEFAULT_STYLE).toExternalForm();
                scene.getStylesheets().add(css);
                stage.setScene(scene);
            } else {
                // replace only the root to preserve scene size and other properties
                scene.setRoot(root);
                // ensure stylesheet is present
                String css = SceneManager.class.getResource(DEFAULT_STYLE).toExternalForm();
                if (!scene.getStylesheets().contains(css)) {
                    scene.getStylesheets().add(css);
                }
            }

            stage.setTitle(title);
            // do not alter resizable/maximized state here to preserve behavior
            stage.getIcons().add(new Image("/images/LogoPrincipal.jpg"));
            stage.show();
        } catch (Exception e) {
            System.err.println("✗ [SceneManager] Falha ao substituir root da cena: " + fxmlPath + " - " + e.getMessage());
        }
    }
}
