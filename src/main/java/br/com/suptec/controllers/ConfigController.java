package br.com.suptec.controllers;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import br.com.suptec.utils.SettingsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Controller da tela de Configurações.
 * Fornece bindings entre os controles da UI (tema, idioma, auto-save) e o
 * serviço de persistência de preferências (SettingsService).
 */
public class ConfigController implements Initializable {

    @FXML
    private ToggleGroup themeToggleGroup;

    @FXML
    private RadioButton rbClaro;

    @FXML
    private RadioButton rbEscuro;

    @FXML
    private CheckBox chkAutoSave;

    @FXML
    private ComboBox<String> comboIdioma;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ensure ToggleGroup exists and is wired to radio buttons (avoid defining non-Node elements in FXML)
        if (themeToggleGroup == null) {
            themeToggleGroup = new ToggleGroup();
        }
        rbClaro.setToggleGroup(themeToggleGroup);
        rbEscuro.setToggleGroup(themeToggleGroup);

        // populate languages
        comboIdioma.getItems().addAll("Português 🇧🇷", "English 🇺🇸");

        // load saved settings
        Properties p = SettingsService.load();
        String theme = p.getProperty("theme", "light");
        if ("dark".equalsIgnoreCase(theme)) {
            rbEscuro.setSelected(true);
        } else {
            rbClaro.setSelected(true);
        }

        String auto = p.getProperty("autoSave", "false");
        chkAutoSave.setSelected(Boolean.parseBoolean(auto));

        String lang = p.getProperty("language", "pt_BR");
        if ("en_US".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang)) {
            comboIdioma.getSelectionModel().select("English 🇺🇸");
        } else {
            comboIdioma.getSelectionModel().select("Português 🇧🇷");
        }

        // if auto-save is enabled, save on change
        chkAutoSave.selectedProperty().addListener((obs, oldV, newV) -> {
            SettingsService.set("autoSave", Boolean.toString(newV));
        });

        rbClaro.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) SettingsService.set("theme", "light");
        });
        rbEscuro.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) SettingsService.set("theme", "dark");
        });

        comboIdioma.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                String code = newV.startsWith("English") ? "en_US" : "pt_BR";
                SettingsService.set("language", code);
            }
        });
    }

    @FXML
    private void onSave() {
        Properties p = SettingsService.load();
        p.setProperty("theme", rbEscuro.isSelected() ? "dark" : "light");
        p.setProperty("autoSave", Boolean.toString(chkAutoSave.isSelected()));
        String lang = comboIdioma.getSelectionModel().getSelectedItem();
        p.setProperty("language", (lang != null && lang.startsWith("English")) ? "en_US" : "pt_BR");
        SettingsService.save(p);
    }

    @FXML
    private void onRestoreDefaults() {
        rbClaro.setSelected(true);
        chkAutoSave.setSelected(false);
        comboIdioma.getSelectionModel().select("Português 🇧🇷");
        onSave();
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Voltar para o menu preservando tamanho e comportamento de redimensionamento do stage
        // Replace root to preserve Stage size
        br.com.suptec.core.SceneManager.replaceRootPreserveStage(stage, "/fxml/MainMenuView.fxml", "SUPTEC - Menu");
        } catch (Exception e) {
            System.err.println("Erro ao voltar para o menu: " + e.getMessage());
        }
    }
}
