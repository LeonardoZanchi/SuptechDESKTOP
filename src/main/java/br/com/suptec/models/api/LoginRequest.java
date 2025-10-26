package br.com.suptec.models.api;

/**
 * Classe para representar a requisição de login enviada para a API
 * Baseada no formato esperado: {"email": "user@example.com", "senha": "string"}
 */
public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest() {}

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}