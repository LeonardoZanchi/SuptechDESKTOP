package br.com.suptec.models.api;

/**
 * Modelo de requisição para adicionar Técnico
 * Endpoint: POST /api/Tecnico/Adicionar
 */
public class TecnicoRequest {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String especialidade;

    public TecnicoRequest() {}

    public TecnicoRequest(String nome, String email, String senha, String telefone, String especialidade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.especialidade = especialidade;
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

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}
