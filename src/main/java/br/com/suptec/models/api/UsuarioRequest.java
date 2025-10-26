package br.com.suptec.models.api;

/**
 * Modelo de requisição para adicionar Usuário comum
 * Endpoint: POST /api/Usuario/Adicionar
 */
public class UsuarioRequest {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String setor;

    public UsuarioRequest() {}

    public UsuarioRequest(String nome, String email, String senha, String telefone, String setor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.setor = setor;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }
}
