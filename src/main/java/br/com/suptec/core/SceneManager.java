package br.com.suptec.core;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Utilitário responsável por carregar e gerenciar cenas (views) da aplicação.
 * Fornece métodos para carregar novas Scenes e também para substituir o root da
 * Scene atual preservando o tamanho e propriedades do Stage.
 * Agora com suporte a dimensionamento responsivo baseado no tamanho da tela.
 */
public class SceneManager {

    private static final String DEFAULT_STYLE = "/css/main.css";
    
    // Percentual da tela que a janela deve ocupar (65% para janelas menores)
    private static final double SCREEN_SIZE_PERCENTAGE = 0.65;
    // Tamanho mínimo da janela (reduzido)
    private static final double MIN_WIDTH = 680;
    private static final double MIN_HEIGHT = 480;

    // Construtor privado para evitar instanciação
    private SceneManager() {} // impede instanciação

    /**
     * Calcula o tamanho ideal da janela baseado no tamanho da tela
     */
    private static Rectangle2D calculateOptimalSize() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        double width = screenBounds.getWidth() * SCREEN_SIZE_PERCENTAGE;
        double height = screenBounds.getHeight() * SCREEN_SIZE_PERCENTAGE;
        
        // Garante tamanho mínimo
        width = Math.max(width, MIN_WIDTH);
        height = Math.max(height, MIN_HEIGHT);
        
        // Garante que não ultrapasse a tela
        width = Math.min(width, screenBounds.getWidth());
        height = Math.min(height, screenBounds.getHeight());
        
        return new Rectangle2D(0, 0, width, height);
    }

    public static void loadScene(Stage stage, String fxmlPath, String title) {
        Rectangle2D size = calculateOptimalSize();
        loadScene(stage, fxmlPath, title, size.getWidth(), size.getHeight(), true, true);
    }

    public static void loadScene(Stage stage, String fxmlPath, String title, double width, double height, boolean resizable) {
        loadScene(stage, fxmlPath, title, width, height, resizable, true);
    }

    public static void loadScene(Stage stage, String fxmlPath, String title, double width, double height, boolean resizable, boolean maximizable) {
        try {
            // Preservar o estado de maximização antes de trocar a cena
            boolean wasMaximized = stage.isMaximized();
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene scene;
            
            // Se não foi especificado tamanho, calcula automaticamente
            if (width <= 0 || height <= 0) {
                Rectangle2D optimalSize = calculateOptimalSize();
                width = optimalSize.getWidth();
                height = optimalSize.getHeight();
            }
            
            scene = new Scene(loader.load(), width, height);
            
            scene.getStylesheets().add(SceneManager.class.getResource(DEFAULT_STYLE).toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.getIcons().add(new Image("/images/LogoPrincipal.jpg"));
            
            // Configurar tamanhos mínimos
            stage.setMinWidth(MIN_WIDTH);
            stage.setMinHeight(MIN_HEIGHT);
            
            stage.setResizable(resizable);
            
            // Desabilitar o botão de maximizar se necessário
            if (!maximizable) {
                stage.setResizable(false);
            }
            
            // Restaurar o estado de maximização
            stage.setMaximized(wasMaximized);
            
            if (!wasMaximized) {
                stage.centerOnScreen();
            }
            
            stage.show();
            
            System.out.println("✅ Janela carregada - Tamanho: " + width + "x" + height + " (Maximizado: " + wasMaximized + ")");
        } catch (Exception e) {
            System.err.println("✗ [SceneManager] Falha ao carregar cena: " + fxmlPath + " - " + e.getMessage());
            e.printStackTrace();
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
