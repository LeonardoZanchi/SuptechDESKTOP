package br.com.suptec.models.api;

/**
 * Classe para representar a resposta de login retornada pela API
 * Baseada no formato: {"token": "jwt_token"}
 */
public class LoginResponse {
    private String token;

    public LoginResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Verifica se o login foi bem-sucedido
     */
    public boolean isSuccess() {
        return token != null && !token.trim().isEmpty();
    }
}