package br.com.suptec.utils;

/**
 * Classe utilitária para validações de campos
 * Centraliza todas as regras de validação da aplicação
 */
public class ValidationUtils {

    /**
     * Valida formato de email
     * @param email Email a ser validado
     * @return true se o email é válido
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        email = email.trim();
        
        // Validação básica: deve conter @ e pelo menos um ponto após o @
        if (!email.contains("@")) {
            return false;
        }
        
        String[] partes = email.split("@");
        if (partes.length != 2) {
            return false;
        }
        
        // Parte antes do @ não pode estar vazia
        if (partes[0].isEmpty()) {
            return false;
        }
        
        // Parte após o @ deve conter pelo menos um ponto
        String dominio = partes[1];
        if (!dominio.contains(".")) {
            return false;
        }
        
        // Validar que há texto antes e depois do ponto no domínio
        String[] partesdominio = dominio.split("\\.");
        if (partesdominio.length < 2) {
            return false;
        }
        
        // Verificar que cada parte do domínio não está vazia
        for (String parte : partesdominio) {
            if (parte.isEmpty()) {
                return false;
            }
        }
        
        // Validar extensões comuns
        String extensao = partesdominio[partesdominio.length - 1].toLowerCase();
        return extensao.matches("(com|br|org|net|edu|gov|mil|int|info|biz|name|pro|com\\.br)");
    }

    /**
     * Valida formato de telefone
     * Aceita telefones fixos e celulares, com ou sem DDD
     * @param telefone Telefone a ser validado
     * @return true se o telefone é válido
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos para validação
        String numeros = telefone.replaceAll("[^0-9]", "");
        
        // Aceitar telefones com:
        // - 8 dígitos: telefone fixo sem DDD (3272-2864)
        // - 9 dígitos: celular sem DDD (99999-9999)
        // - 10 dígitos: telefone fixo com DDD (11 3272-2864)
        // - 11 dígitos: celular com DDD (11 99999-9999)
        return numeros.length() >= 8 && numeros.length() <= 11;
    }

    /**
     * Valida que o nome contém apenas letras
     * @param nome Nome a ser validado
     * @return true se o nome é válido
     */
    public static boolean validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        
        nome = nome.trim();
        
        // Nome deve ter pelo menos 3 caracteres e conter apenas letras e espaços
        if (nome.length() < 3) {
            return false;
        }
        return nome.matches("[a-zA-ZÀ-ÿ\\s]+");
    }

    /**
     * Valida que a senha tem o mínimo de caracteres
     * @param senha Senha a ser validada
     * @param minCaracteres Número mínimo de caracteres
     * @return true se a senha é válida
     */
    public static boolean validarSenha(String senha, int minCaracteres) {
        if (senha == null || senha.trim().isEmpty()) {
            return false;
        }
        return senha.trim().length() >= minCaracteres;
    }

    /**
     * Valida que o texto contém apenas letras e espaços
     * @param texto Texto a ser validado
     * @return true se o texto é válido
     */
    public static boolean validarTextoApenasLetras(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return texto.trim().matches("[a-zA-ZÀ-ÿ\\s]+");
    }

    /**
     * Valida que o campo não está vazio
     * @param campo Campo a ser validado
     * @return true se o campo não está vazio
     */
    public static boolean validarCampoObrigatorio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
}
