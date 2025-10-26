package br.com.suptec.utils;

import javafx.scene.control.TextField;

/**
 * Classe utilitária para configurar validações em tempo real nos campos de formulário
 * Aplica máscaras e restrições de entrada durante a digitação
 */
public class FieldValidator {

    /**
     * Configura validação de nome (apenas letras e espaços)
     * @param textField Campo de texto a ser configurado
     */
    public static void configurarValidacaoNome(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Configura validação de telefone (apenas números, parênteses, hífen e espaço)
     * @param textField Campo de texto a ser configurado
     */
    public static void configurarValidacaoTelefone(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[0-9()\\-\\s]*")) {
                textField.setText(oldValue);
            }
            // Limitar a 15 caracteres (formato: (99) 99999-9999)
            if (newValue != null && newValue.length() > 15) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Configura validação de email (caracteres válidos para email)
     * @param textField Campo de texto a ser configurado
     */
    public static void configurarValidacaoEmail(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[a-zA-Z0-9@._\\-]*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Configura validação para campos que aceitam apenas letras e espaços
     * @param textField Campo de texto a ser configurado
     */
    public static void configurarValidacaoTextoApenasLetras(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Configura validação para campos numéricos
     * @param textField Campo de texto a ser configurado
     * @param maxLength Tamanho máximo do campo (0 para ilimitado)
     */
    public static void configurarValidacaoNumerica(TextField textField, int maxLength) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[0-9]*")) {
                textField.setText(oldValue);
            }
            if (maxLength > 0 && newValue != null && newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Configura validação alfanumérica (letras e números)
     * @param textField Campo de texto a ser configurado
     */
    public static void configurarValidacaoAlfanumerica(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[a-zA-Z0-9\\s]*")) {
                textField.setText(oldValue);
            }
        });
    }

    /**
     * Limpa o conteúdo de um campo
     * @param textField Campo de texto a ser limpo
     */
    public static void limparCampo(TextField textField) {
        textField.clear();
    }
}
